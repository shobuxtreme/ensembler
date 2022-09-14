import { Flex } from '@chakra-ui/react';
import { Navbar } from 'components/Navbar';

export const Main = ({ children, router }) => {
  return (
    <Flex as="main" minH="100vh" flexDirection="column">
      <Navbar />
      <Flex flexDirection="column" flexGrow="1">
        {children}
      </Flex>
    </Flex>
  );
};
