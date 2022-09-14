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

export default function LoadJobModal(props) {
  return (
    <Modal onClose={props.onClose} size="xl" isOpen={props.view}>
      <ModalOverlay />
      <ModalContent>
        <ModalHeader>Load Job from JSON</ModalHeader>
        <ModalCloseButton />
        <ModalBody>
          <Box boxShadow="base" rounded="md" bg="white" p={2} minW="md">
            <VStack spacing={3}>
              <InputGroup>
                <Textarea
                  name="jobJson"
                  placeholder="Paste job JSON"
                  minH={'lg'}
                  value={props.data}
                  onChange={event => {
                    props.handleChange(event.target.value);
                  }}
                />
              </InputGroup>
            </VStack>
          </Box>
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
            <Button colorScheme={'blue'} onClick={props.loadJobEvent}>
              Load
            </Button>
          </HStack>
        </ModalFooter>
      </ModalContent>
    </Modal>
  );
}
