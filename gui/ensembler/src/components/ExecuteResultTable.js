import React, { useEffect } from 'react';
import {
  Table,
  Thead,
  Tbody,
  Tr,
  Th,
  Td,
  TableContainer,
  IconButton,
  Tfoot,
  Text,
  HStack,
  Tooltip,
  Badge,
  Spinner,
} from '@chakra-ui/react';
import { DeleteIcon, RepeatIcon } from '@chakra-ui/icons';
import { getResultsForJob } from 'api/Api';

const row = response => {
  return (
    <Tr key={response.id}>
      <Td>{response.kind}</Td>
      <Td>
        <Tooltip label={response.modelId}>{response.model.modelName}</Tooltip>
      </Td>
      <Td>
        <Badge colorScheme={response.status === 'SUCCESS' ? 'green' : 'red'}>
          {response.status}
        </Badge>
      </Td>
      <Td>
        <Tooltip label={response.execResultMsg}>
          {response.execResultMsg.slice(0, 30)}
        </Tooltip>
      </Td>
      <Td isNumeric>{`${response.execTime / 1000} s`}</Td>
    </Tr>
  );
};

export default function ExecuteResultTable(props) {
  const [data, setData] = React.useState(props.initialData);
  const [fetcher, setFetcher] = React.useState(null);

  const clearResults = () => {
    setData([]);
    props.clearExecute();
  };

  useEffect(() => {
    if (props.executing) {
      //Executing
      console.log('test A');
      const interval = setInterval(() => {
        fetchResults();
      }, 5000);

      setFetcher(interval);
    } else {
      //Stop Auto fetch
      console.log('test B');
      if (fetcher != null) clearInterval(fetcher);

      setFetcher(null);
    }
  }, [props.executing]);

  //-----API Functions-----//
  const fetchResults = () => {
    let jobResponse = JSON.parse(window.localStorage.getItem('jobResponse'));

    if (jobResponse === null) {
      console.log('jobResponse null');
      return;
    }

    let id = jobResponse.jobId;
    var res = getResultsForJob(id);
    var kind = jobResponse.jobKind;
    var expectedResults = 0;

    Object.values(jobResponse.modelSet).forEach(set => {
      expectedResults = expectedResults + set.replicas;
    });

    if (kind === 'TRAIN') {
      expectedResults = 2 * expectedResults;
    }

    res.then(response => {
      console.log(response.data);
      setData(response.data);

      if (response.data.length === expectedResults) {
        clearInterval(fetcher);

        props.clearExecute();
      }
    });
  };

  return (
    <TableContainer boxShadow="dark-lg" rounded="md">
      <Table minW="lg" flexGrow={1} variant="simple">
        <Thead>
          <Tr>
            <Th>Kind</Th>
            <Th>Model</Th>
            <Th>Status</Th>
            <Th>Message</Th>
            <Th isNumeric>Execute Time</Th>
          </Tr>
        </Thead>

        {data.length === 0 ? (
          <Tbody p={5}>
            <Tr>
              <Td>
                <Text>No results available.</Text>
              </Td>
            </Tr>
          </Tbody>
        ) : (
          <Tbody>
            {data.map(obj => {
              return row(obj);
            })}
          </Tbody>
        )}

        <Tfoot>
          <HStack spacing={2} p={2}>
            <IconButton
              icon={<RepeatIcon />}
              colorScheme="orange"
              variant="outline"
              onClick={fetchResults}
            />
            <IconButton
              icon={<DeleteIcon />}
              colorScheme="orange"
              variant="outline"
              onClick={clearResults}
            />
            {props.executing ? (
              <>
                <Spinner color="red.500" size="lg" />
                <Text>Fetching results...</Text>
              </>
            ) : null}
          </HStack>
        </Tfoot>
      </Table>
    </TableContainer>
  );
}
