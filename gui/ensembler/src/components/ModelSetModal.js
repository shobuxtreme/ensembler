import {
  Box,
  Button,
  HStack,
  Input,
  InputGroup,
  InputLeftAddon,
  Modal,
  ModalBody,
  ModalCloseButton,
  ModalContent,
  ModalFooter,
  ModalHeader,
  ModalOverlay,
  Text,
  VStack,
} from '@chakra-ui/react';
import React from 'react';

export default function ModelSetModal(props) {
  return (
    <Modal onClose={props.onClose} size="xl" isOpen={props.view}>
      <ModalOverlay />
      <ModalContent>
        <ModalHeader>Model Set</ModalHeader>
        <ModalCloseButton />
        <ModalBody>
          <ModelSet
            data={props.data}
            handleChange={props.handleChange}
          ></ModelSet>
        </ModalBody>
        <ModalFooter>
          <HStack spacing={2}>
            <Button
              colorScheme={'orange'}
              variant="outline"
              onClick={props.onClose}
            >
              Cancel
            </Button>
            <Button
              colorScheme={'blue'}
              onClick={() => props.saveSetEvent(null)}
            >
              Save
            </Button>
          </HStack>
        </ModalFooter>
      </ModalContent>
    </Modal>
  );
}

const ModelSet = props => {
  return (
    <Box boxShadow="base" rounded="md" bg="white" p={2} minW="md">
      <VStack spacing={3}>
        <Text alignSelf={'flex-start'} fontSize="sm" fontWeight={'bold'}>
          Set Name:
        </Text>
        <InputGroup>
          <Input
            name="name"
            placeholder="Set Name"
            value={props.data.name}
            onChange={event => {
              props.handleChange(event.target.name, event.target.value);
            }}
          />
        </InputGroup>
        <Text alignSelf={'flex-start'} fontSize="sm" fontWeight={'bold'}>
          Level:
        </Text>
        <InputGroup>
          <Input
            name="level"
            type="number"
            placeholder="Level"
            value={props.data.level}
            onChange={event => {
              props.handleChange(event.target.name, event.target.value);
            }}
          />
        </InputGroup>
        <Text alignSelf={'flex-start'} fontSize="sm" fontWeight={'bold'}>
          Model Name:
        </Text>
        <InputGroup>
          <Input
            name="modelName"
            placeholder="Model Name"
            value={props.data.modelName}
            onChange={event => {
              props.handleChange(event.target.name, event.target.value);
            }}
          />
        </InputGroup>
        <Text alignSelf={'flex-start'} fontSize="sm" fontWeight={'bold'}>
          Container Image:
        </Text>
        <InputGroup>
          <Input
            name="modelImage"
            value={props.data.modelImage}
            placeholder="Model Image"
            onChange={event => {
              props.handleChange(event.target.name, event.target.value);
            }}
          />
        </InputGroup>
        <Text alignSelf={'flex-start'} fontSize="sm" fontWeight={'bold'}>
          Commands:
        </Text>

        <InputGroup>
          <InputLeftAddon children="CMD" />
          <Input
            name="trainCmd"
            value={props.data.trainCmd}
            placeholder="Enter TRAIN Command"
            onChange={event => {
              props.handleChange(event.target.name, event.target.value);
            }}
          />
        </InputGroup>

        <InputGroup>
          <InputLeftAddon children="CMD" />
          <Input
            name="validateCmd"
            value={props.data.validateCmd}
            placeholder="Enter VALIDATE Command"
            onChange={event => {
              props.handleChange(event.target.name, event.target.value);
            }}
          />
        </InputGroup>

        <InputGroup>
          <InputLeftAddon children="CMD" />
          <Input
            name="predictCmd"
            value={props.data.predictCmd}
            placeholder="Enter PREDICT Command"
            onChange={event => {
              props.handleChange(event.target.name, event.target.value);
            }}
          />
        </InputGroup>

        <Text alignSelf={'flex-start'} fontSize="sm" fontWeight={'bold'}>
          Replicas:
        </Text>

        <InputGroup>
          <Input
            name="replicas"
            type="number"
            value={props.data.replicas}
            placeholder="Replicas"
            onChange={event => {
              props.handleChange(event.target.name, event.target.value);
            }}
          />
        </InputGroup>
      </VStack>
    </Box>
  );
};
