/*
import {
  Box,
  Button,
  HStack,
  InputGroup,
  Modal,
  ModalBody,
  ModalCloseButton,
  ModalContent,
  ModalFooter,
  ModalHeader,
  ModalOverlay,
  Textarea,
  VStack,
} from '@chakra-ui/react';
import React from 'react';
import Tree from 'react-d3-tree';

export default function EnsembleTreeModal(props) {
  const tree = set => {
    //[2,1,1,0,0,0,0]

    var nodes = Object.values(set)
      .sort((val1, val2) => {
        return val1.level - val2.level;
      })
      .reverse();

    var level = nodes[0].leve;
  };

  const test = {
    name: 'l1',
    children: [
      {
        name: 'l1',
      },
      {
        name: 'l1',
      },
      {
        name: 'c',
      },
    ],
  };

  return (
    <Modal onClose={props.onClose} isOpen={props.view} size={'xl'}>
      <ModalOverlay />
      <ModalContent>
        <ModalHeader>Ensemble Configuration</ModalHeader>
        <ModalCloseButton />
        <ModalBody>
          <Box boxShadow="base" rounded="md" bg="white" p={2} w="lg" h="2xl">
            <Tree
              data={test}
              zoom={1}
              translate={{ x: 400, y: 300 }}
              depthFactor={-100}
            />
          </Box>
        </ModalBody>
        <ModalFooter>
          <HStack spacing={2}>
            <Button
              colorScheme={'orange'}
              variant="outline"
              onClick={props.onClose}
            >
              Close
            </Button>
          </HStack>
        </ModalFooter>
      </ModalContent>
    </Modal>
  );
}
*/
