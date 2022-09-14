import {
  Box,
  Button,
  Input,
  InputGroup,
  InputLeftAddon,
  Menu,
  MenuItem,
  MenuList,
  Select,
  Spinner,
  Stack,
  Text,
  useToast,
  VStack,
} from '@chakra-ui/react';
import React from 'react';
import { execute } from 'api/Api';
import ModelSetsTable from 'components/ModelSetsTable';
import {
  AddIcon,
  ArrowForwardIcon,
  AttachmentIcon,
  DeleteIcon,
} from '@chakra-ui/icons';
import ModelSetModal from './ModelSetModal';
import LoadJobModal from './LoadJobModal';

const jobTemplate = {
  jobName: '',
  trainDataSource: '',
  validateDataSource: '',
  predictDataSource: '',
  factorizeCmd: '',
  jobKind: '',
  modelSet: {},
};

const dataTemplate = {
  name: '',
  level: '',
  modelName: '',
  modelImage: '',
  trainCmd: '',
  validateCmd: '',
  predictCmd: '',
  replicas: '',
  tuneParameters: false,
};

export default function JobConfig(props) {
  const [config, setConfig] = React.useState(jobTemplate);
  const [viewAddSet, setViewAddSet] = React.useState(false);
  const [modelSet, setModelSet] = React.useState(dataTemplate);
  const [viewJobJson, setViewJobJson] = React.useState(false);
  const [jobJson, setJobJson] = React.useState('');
  const toast = useToast();

  const resetState = () => {
    setConfig({ ...jobTemplate });
    props.reset();
  };

  const handleConfig = (key, val) => {
    setConfig({ ...config, [key]: val });
  };

  const handleModelSet = (key, val) => {
    setModelSet({ ...modelSet, [key]: val });
  };

  const saveModelSet = name => {
    let setName = name === null ? modelSet.name : name;

    setConfig({
      ...config,
      modelSet: {
        ...config.modelSet,
        [setName]: modelSetToBo(modelSet),
      },
    });

    closeAddSet();
  };

  const openJobJsonModal = () => {
    setViewJobJson(true);
  };

  const closeJobJsonModal = () => {
    setViewJobJson(false);
    setJobJson('');
  };

  const handleJobJson = value => {
    setJobJson(value);
  };

  const duplicateModelSet = data => {
    let name = 'copy.set.' + Object.keys(config.modelSet).length;

    setConfig({
      ...config,
      modelSet: {
        ...config.modelSet,
        [name]: data,
      },
    });
  };

  const editModelSet = (name, set) => {
    let modelSet = flattenModelSet(name, set);
    setModelSet(modelSet);
    openAddSet();
  };

  const removeModelSet = setName => {
    let data = { ...config.modelSet };
    delete data[setName];
    setConfig({
      ...config,
      modelSet: data,
    });
  };

  const openAddSet = () => {
    setViewAddSet(true);
  };

  const closeAddSet = () => {
    setViewAddSet(false);
    setModelSet({ ...dataTemplate });
  };

  const loadConfigFromJson = () => {
    var jsonConfig;
    try {
      jsonConfig = JSON.parse(jobJson);
      console.log(jsonConfig);
      setConfig({ ...jsonConfig });
      closeJobJsonModal();
    } catch (e) {
      console.log(e);

      toast({
        title: 'Load Job from JSON unsuccessful.',
        description: e.message,
        status: 'error',
        duration: 5000,
        isClosable: true,
      });
    }
  };

  //-----API Functions-----//
  const executeRequest = () => {
    if (config.validateDataSource === '' || config.validateDataSource == null) {
      setConfig({ ...config, validateDataSource: config.trainDataSource });
    }
    if (config.predictDataSource === '' || config.predictDataSource == null) {
      setConfig({ ...config, predictDataSource: config.trainDataSource });
    }
    console.log('Request: ', config);

    var res = execute(config);
    props.executeRequest();

    res.then(response => {
      window.localStorage.setItem('jobResponse', JSON.stringify(response.data));
      console.log('Response: ', response.data);
    });
  };

  //-----Helper Functions-----//
  const flattenModelSet = (name, set) => {
    return {
      name: name,
      level: set.level,
      modelName: set.model.modelName,
      modelImage: set.model.modelImage,
      trainCmd: set.model.trainCmd,
      validateCmd: set.model.validateCmd,
      predictCmd: set.model.predictCmd,
      replicas: set.replicas,
      tuneParameters: set.tuneParameters,
    };
  };

  const modelSetToBo = modelSet => {
    let model = {
      modelName: modelSet.modelName,
      modelImage: modelSet.modelImage,
      trainCmd: modelSet.trainCmd,
      validateCmd: modelSet.validateCmd,
      predictCmd: modelSet.predictCmd,
    };

    let set = {
      level: modelSet.level,
      model: model,
      replicas: modelSet.replicas,
      tuneParameters: modelSet.tuneParameters,
    };

    return set;
  };
  return (
    <Box boxShadow="dark-lg" rounded="md" p={2} minW="md">
      <VStack spacing={2}>
        <Text fontWeight={'bold'}>Job Configuration</Text>

        <Text alignSelf={'flex-start'} fontSize="sm" fontWeight={'bold'}>
          Job Name:
        </Text>
        <Input
          value={config.jobName}
          name="jobName"
          placeholder="Enter Job Name"
          onChange={event => {
            handleConfig(event.target.name, event.target.value);
          }}
        />

        <Text alignSelf={'flex-start'} fontSize="sm" fontWeight={'bold'}>
          Factorizer Data URLs:
        </Text>

        <InputGroup>
          <InputLeftAddon children="URL" />
          <Input
            value={config.trainDataSource}
            name="trainDataSource"
            placeholder="Enter TRAIN Data Source"
            onChange={event => {
              handleConfig(event.target.name, event.target.value);
            }}
          />
        </InputGroup>

        <InputGroup>
          <InputLeftAddon children="URL" />
          <Input
            value={config.validateDataSource}
            name="validateDataSource"
            placeholder="Enter VALIDATE Data Source"
            onChange={event => {
              handleConfig(event.target.name, event.target.value);
            }}
          />
        </InputGroup>

        <InputGroup>
          <InputLeftAddon children="URL" />
          <Input
            value={config.predictDataSource}
            name="predictDataSource"
            placeholder="Enter PREDICT Data Source"
            onChange={event => {
              handleConfig(event.target.name, event.target.value);
            }}
          />
        </InputGroup>
        <Text alignSelf={'flex-start'} fontSize="sm" fontWeight={'bold'}>
          Factorize Command:
        </Text>
        <InputGroup>
          <InputLeftAddon children="CMD" />
          <Input
            value={config.factorizeCmd}
            name="factorizeCmd"
            placeholder="Enter Factorize CMD"
            onChange={event => {
              handleConfig(event.target.name, event.target.value);
            }}
          />
        </InputGroup>

        <Text alignSelf={'flex-start'} fontSize="sm" fontWeight={'bold'}>
          Job Kind:
        </Text>
        <Select
          name="jobKind"
          value={config.jobKind}
          placeholder="Select Job Kind"
          onChange={event => {
            handleConfig(event.target.name, event.target.value);
          }}
        >
          <option value="TRAIN">TRAIN</option>
          <option value="VALIDATE">VALIDATE</option>
          <option value="PREDICT">PREDICT</option>
        </Select>

        <Text alignSelf={'flex-start'} fontSize="sm" fontWeight={'bold'}>
          Models:
        </Text>

        <Box alignSelf={'flex-start'}>
          <Menu alignSelf="flex-start">
            <Button as={Button} rightIcon={<AddIcon />} onClick={openAddSet}>
              Add Model Set
            </Button>
            <MenuList>
              <MenuItem>Edit</MenuItem>
              <MenuItem>Duplicate</MenuItem>
              <MenuItem>Delete</MenuItem>
            </MenuList>
          </Menu>
        </Box>

        <ModelSetsTable
          data={config.modelSet}
          duplicateSetEvent={duplicateModelSet}
          deleteSetEvent={removeModelSet}
          editSetEvent={editModelSet}
        ></ModelSetsTable>

        <Stack alignSelf="flex-start" direction="row" spacing={4}>
          <Button
            rightIcon={<DeleteIcon />}
            colorScheme="orange"
            variant="outline"
            onClick={resetState}
          >
            Reset
          </Button>
          <Button
            rightIcon={<AttachmentIcon />}
            colorScheme="orange"
            variant="outline"
            onClick={openJobJsonModal}
          >
            Load
          </Button>

          <Button
            rightIcon={<ArrowForwardIcon />}
            colorScheme="blue"
            onClick={executeRequest}
          >
            Execute
          </Button>

          {props.executing ? (
            <>
              <Spinner color="red.500" size="lg" />
            </>
          ) : null}
        </Stack>
      </VStack>

      <ModelSetModal
        data={modelSet}
        view={viewAddSet}
        handleChange={handleModelSet}
        saveSetEvent={saveModelSet}
        onClose={closeAddSet}
      ></ModelSetModal>

      <LoadJobModal
        view={viewJobJson}
        data={jobJson}
        onClose={closeJobJsonModal}
        handleChange={handleJobJson}
        loadJobEvent={loadConfigFromJson}
      ></LoadJobModal>
    </Box>
  );
}
