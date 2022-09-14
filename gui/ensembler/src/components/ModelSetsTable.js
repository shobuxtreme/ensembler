import React from 'react';
import {
  Table,
  Thead,
  Tbody,
  Tr,
  Th,
  Td,
  TableContainer,
  Menu,
  MenuButton,
  MenuList,
  MenuItem,
  IconButton,
  useColorModeValue,
} from '@chakra-ui/react';

import { SettingsIcon } from '@chakra-ui/icons';

const row = (key, value, i, props) => {
  return (
    <Tr key={value.model.modelName + i}>
      <Td isNumeric>{value.level}</Td>
      <Td>{value.model.modelName}</Td>
      <Td isNumeric>{value.replicas}</Td>
      <Td>
        <Menu>
          <MenuButton as={IconButton} icon={<SettingsIcon />}></MenuButton>
          <MenuList>
            <MenuItem onClick={() => props.editSetEvent(key, value)}>
              Edit
            </MenuItem>
            <MenuItem onClick={() => props.duplicateSetEvent(value)}>
              Duplicate
            </MenuItem>
            <MenuItem onClick={() => props.deleteSetEvent(key)}>
              Delete
            </MenuItem>
          </MenuList>
        </Menu>
      </Td>
    </Tr>
  );
};

export default function ModelSetsTable(props) {
  return (
    <TableContainer bg={useColorModeValue()} minW="md">
      <Table variant="simple">
        <Thead>
          <Tr>
            <Th isNumeric>Level</Th>
            <Th>Model</Th>
            <Th isNumeric>Replicas</Th>
            <Th></Th>
          </Tr>
        </Thead>
        <Tbody>
          {Object.entries(props.data).map(([key, value], i) => {
            return row(key, value, i, props);
          })}
        </Tbody>
      </Table>
    </TableContainer>
  );
}
