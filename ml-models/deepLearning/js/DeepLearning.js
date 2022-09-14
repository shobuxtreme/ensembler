const tf = require("@tensorflow/tfjs");
require("@tensorflow/tfjs-node");
const yargs = require("yargs");
const { performance } = require("perf_hooks");
const fs = require("fs");
const archiver = require("archiver");
const jszip = require("jszip");

// Train Command
yargs.command({
  command: "train",
  builder: {
    epochs: {
      demandOption: true,
      type: "number",
    },
    batchsize: {
      demandOption: true,
      type: "number",
    },
    lossfn: {
      demandOption: true,
      type: "string",
    },
    y: {
      demandOption: true,
      type: "string",
    },
  },

  // Function for train command
  async handler(argv) {
    var startTime = performance.now();

    // 1. Load dataset
    const label = argv.y;
    const dataSet = await loadDataset("train.csv", label);

    // Prepare the Dataset for training.
    const flattenedDataset = dataSet
      .map(({ xs, ys }) => {
        // Convert xs(features) and ys(labels) from object form (keyed by
        // column name) to array form.
        return { xs: Object.values(xs), ys: Object.values(ys) };
      })
      .batch(argv.batchsize);

    // Train a DNN model:

    const model = tf.sequential();
    model.add(
      tf.layers.dense({ units: 12, activation: "relu", inputShape: [8] })
    );
    model.add(tf.layers.dense({ units: 8, activation: "relu" }));
    model.add(tf.layers.dense({ units: 1, activation: "sigmoid" }));
    model.compile({ optimizer: "adam", loss: "binaryCrossentropy" });

    // Fit the model using the prepared Dataset
    const info = await model.fitDataset(flattenedDataset, {
      epochs: argv.epochs,
      verbose: 0,
      callbacks: {
        onEpochEnd: async (epoch, logs) => {
          //console.log(epoch + ":" + logs.loss);
        },
      },
    });

    // Save Model
    await saveModel(model);
    console.log("Model saved to disk.");

    var endTime = performance.now();
    var timeElapsed = endTime - startTime;
    console.log(`Traininng finished in ${timeElapsed / 1000} s`);
  },
});

// Validate Command
yargs.command({
  command: "validate",
  builder: {
    batchsize: {
      demandOption: true,
      type: "number",
    },
    y: {
      demandOption: true,
      type: "string",
    },
  },

  // Function for Validate command
  async handler(argv) {
    var startTime = performance.now();

    // 1. Load dataset
    const label = argv.y;
    const dataSet = await loadDataset("validate.csv", label);

    const validateDataset = dataSet
      .mapAsync(({ xs, ys }) => {
        // Convert xs(features) and ys(labels) from object form (keyed by
        // column name) to array form.
        return { xs: Object.values(xs), ys: Object.values(ys) };
      })
      .batch(10);

    const input = await validateDataset.toArray();

    // 2. Load Model
    const model = await loadModel();

    // 3. Validate & store
    var i = 0;
    var size = 0;
    var matches = 0;

    let csvContent = "";
    csvContent += ",0" + "\r\n";

    const binaryAccuracy = input.map(({ xs, ys }) => {
      const yPred = model.predict(xs, { batchSize: argv.batchsize });

      yPred.arraySync().forEach((res) => {
        let row = `${i},${Math.round(res)}`;
        csvContent += row + "\r\n";
        ++i;
      });

      const accuracy = tf.metrics.binaryAccuracy(ys, yPred);
      size = size + accuracy.size;
      matches = matches + accuracy.arraySync().filter((i) => i === 1).length;
      return accuracy;
    });

    const result = matches / size;

    const fileName = "validate.results.csv";
    await saveCSVFile(csvContent, fileName);

    console.log(`Accuracy: ${result}`);

    var endTime = performance.now();
    var timeElapsed = endTime - startTime;
    console.log(`Validation finished in ${timeElapsed / 1000} s`);
  },
});

// Predict Command
yargs.command({
  command: "predict",
  builder: {
    batchsize: {
      demandOption: true,
      type: "number",
    },
    y: {
      demandOption: true,
      type: "string",
    },
  },

  // Function for Predict command
  async handler(argv) {
    var startTime = performance.now();

    // 1. Load dataset
    const label = argv.y;
    const dataSet = await loadDataset("predict.csv", label);

    const predictDataset = dataSet
      .mapAsync(({ xs, ys }) => {
        // Convert xs(features) and ys(labels) from object form (keyed by
        // column name) to array form.
        return { xs: Object.values(xs), ys: Object.values(ys) };
      })
      .batch(10);

    const input = await predictDataset.toArray();

    // 2. Load Model
    const model = await loadModel();

    // 3. Validate & store
    var i = 0;

    let csvContent = "";
    csvContent += ",0" + "\r\n";

    const predictResults = input.map(({ xs, ys }) => {
      const yPred = model.predict(xs, { batchSize: argv.batchsize });

      yPred.arraySync().forEach((res) => {
        let row = `${i},${Math.round(res)}`;
        csvContent += row + "\r\n";
        ++i;
      });
    });

    const fileName = "predict.results.csv";
    await saveCSVFile(csvContent, fileName);

    var endTime = performance.now();
    var timeElapsed = endTime - startTime;
    console.log(`Prediction finished in ${timeElapsed / 1000} s`);
  },
});

yargs.parse(); // To set above changes

// Load dataset
async function loadDataset(name, label) {
  //const url = `file://${__dirname}/${name}`;
  const url = `file://./${name}`;
  const dataSet = tf.data.csv(url, {
    hasHeader: true,
    configuredColumnsOnly: true,
    columnConfigs: {
      Pregnancies: {
        required: true,
      },
      Glucose: {
        required: true,
      },
      BloodPressure: {
        required: true,
      },
      SkinThickness: {
        required: true,
      },
      Insulin: {
        required: true,
      },
      BMI: {
        required: true,
      },
      DiabetesPedigreeFunction: {
        required: true,
      },
      Age: {
        required: true,
      },
      [label]: {
        isLabel: true,
      },
    },
  });

  return dataSet;
}

// Save model
async function saveModel(model) {
  const name = "latest.model";
  const url = `file://./${name}`;
  await model.save(url);
  compressModel();
}

// Archiver

function compressModel() {
  const output = fs.createWriteStream("latest.model.joblib");
  const archive = archiver("zip", {
    zlib: { level: 9 }, // Sets the compression level.
  });

  output.on("close", function () {
    console.log(archive.pointer() + " bytes, Model saved to disk.");
  });

  // This event is fired when the data source is drained no matter what was the data source.
  // It is not part of this library but rather from the NodeJS Stream API.
  // @see: https://nodejs.org/api/stream.html#stream_event_end
  output.on("end", function () {
    console.log("Data has been drained");
  });

  // good practice to catch warnings (ie stat failures and other non-blocking errors)
  archive.on("warning", function (err) {
    if (err.code === "ENOENT") {
      // log warning
    } else {
      // throw error
      throw err;
    }
  });

  // good practice to catch this error explicitly
  archive.on("error", function (err) {
    throw err;
  });

  // pipe archive data to the file
  archive.pipe(output);

  archive.directory("latest.model/", false);

  archive.finalize();
}

async function decompressModel() {
  const fileContent = fs.readFileSync("latest.model.joblib");
  const jszipInstance = new jszip();

  const result = await jszipInstance.loadAsync(fileContent);
  const keys = Object.keys(result.files);
  for (let k of keys) {
    const item = result.files[k];
    if (item.dir) {
      fs.mkdirSync(item.name);
    } else {
      fs.writeFileSync(item.name, Buffer.from(await item.async("arraybuffer")));
    }
  }
}

// Load model
async function loadModel() {
  //Decompress Model
  await decompressModel();
  const url = `file://./model.json`;
  return await tf.loadLayersModel(url);
}

// Write File
async function saveCSVFile(data, name) {
  fs.writeFile(name, data, (err) => {
    if (err) return console.error(err);
    console.log("Data saved to disk.");
  });
}
