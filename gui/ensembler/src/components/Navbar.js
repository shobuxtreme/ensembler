import { Box, Heading, Spacer, Stack } from '@chakra-ui/react';
import { ColorModeSwitcher } from 'components/ColorModeSwitcher';

export const Navbar = () => {
  return (
    <Box as="nav" boxShadow="md">
      <Stack direction="row" alignItems="center" p={1}>
        <Heading as="h4" size="md" p={2}>
          enseMbLer
        </Heading>
        <Spacer />
        <ColorModeSwitcher />
      </Stack>
    </Box>
  );
};
