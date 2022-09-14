import React from 'react';
import { ChakraProvider, Stack, theme } from '@chakra-ui/react';
import { Main } from 'layouts/Main';
import JobConfig from 'components/JobConfig';
import ExecuteResultTable from 'components/ExecuteResultTable';

function App() {
  console.log(process.env.REACT_APP_EXECUTE_API_URL_BASE);
  console.log(process.env.REACT_APP_DATA_API_URL_BASE);

  const [executing, setExecuting] = React.useState(false);

  const executeRequest = () => {
    setExecuting(true);
  };

  const clearExecute = () => {
    setExecuting(false);
  };

  const reset = () => {
    clearExecute();
    window.localStorage.removeItem('jobResponse');
  };

  return (
    <ChakraProvider theme={theme}>
      <Main>
        <Stack
          spacing={2}
          p={2}
          direction="row"
          flexGrow={1}
          flexWrap={1}
          overflowX="auto"
        >
          <Stack minW="md" p={2}>
            <JobConfig executeRequest={executeRequest} reset={reset} />
          </Stack>

          <Stack minW="lg" flexGrow={1} flexShrink={0} p={2}>
            <ExecuteResultTable
              executing={executing}
              clearExecute={clearExecute}
              initialData={[]}
            />
          </Stack>
        </Stack>
      </Main>
    </ChakraProvider>
  );
}

export default App;
