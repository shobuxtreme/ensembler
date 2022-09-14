const tf = require("@tensorflow/tfjs");
require("@tensorflow/tfjs-node");
const yargs = require("yargs");
const { performance } = require("perf_hooks");
const fs = require("fs");

// Vote Command
yargs.command({
  command: "vote",
  builder: {
    inputs: {
      demandOption: true,
      type: "number",
    },
  },

  // Function for train command
  async handler(argv) {
    var startTime = performance.now();

    const inputs = argv.inputs;
    let data = [];

    // Load datasets
    for (let x = 1; x <= inputs; ++x) {
      let dataset = await loadDataset("predict." + x.toString() + ".csv", "0");

      const res = dataset.mapAsync(({ xs, ys }) => {
        return Object.values(ys)[0];
      });
      data.push(await res.toArray());
    }

    // Save CSV
    let csvContent = "";
    csvContent += ",0" + "\r\n";

    // Max Vote
    let result = [];
    for (let x = 0; x < data[0].length; ++x) {
      let vectorRow = [];
      data.forEach((arr) => {
        vectorRow.push(arr[x]);
      });

      let res = findMajority(vectorRow);
      if (res == -1) {
        res = vectorRow[0];
      }
      result.push(res);

      let row = `${x},${res}`;
      csvContent += row + "\r\n";
    }

    const fileName = "predict.results.csv";
    await saveCSVFile(csvContent, fileName);

    var endTime = performance.now();
    var timeElapsed = endTime - startTime;
    console.log(`Voting finished in ${timeElapsed / 1000} s`);
  },
});

// Train
yargs.command({
  command: "train",
  builder: {},

  // Function for train command
  async handler() {
    var startTime = performance.now();

    const fileName = "latest.model.joblib";
    await saveCSVFile("", fileName);

    var endTime = performance.now();
    var timeElapsed = endTime - startTime;
    console.log(`Max Vote in ${timeElapsed / 1000} s`);
  },
});

// Validate
yargs.command({
  command: "validate",
  builder: {},

  // Function for train command
  async handler() {
    var startTime = performance.now();

    const fileName = "validate.results.csv";
    await saveCSVFile("", fileName);

    var endTime = performance.now();
    var timeElapsed = endTime - startTime;
    console.log(`Max Vote in ${timeElapsed / 1000} s`);
  },
});

yargs.parse(); // To set above changes

// Boyer-Moore Max Vote
function findMajority(nums) {
  var count = 0,
    candidate = -1;

  // Finding majority candidate
  for (var index = 0; index < nums.length; index++) {
    if (count == 0) {
      candidate = nums[index];
      count = 1;
    } else {
      if (nums[index] == candidate) count++;
      else count--;
    }
  }

  count = 0;
  for (var index = 0; index < nums.length; index++) {
    if (nums[index] == candidate) count++;
  }
  if (count > nums.length / 2) return candidate;
  return -1;
}

// Load dataset
async function loadDataset(name, label) {
  //const url = `file://${__dirname}/${name}`;
  const url = `file://./${name}`;
  const dataSet = tf.data.csv(url, {
    hasHeader: true,
    configuredColumnsOnly: true,
    columnConfigs: {
      [label]: {
        required: true,
        isLabel: true,
      },
    },
  });

  return dataSet;
}

// Write File
async function saveCSVFile(data, name) {
  fs.writeFile(name, data, (err) => {
    if (err) return console.error(err);
    console.log("Data saved to disk.");
  });
}
