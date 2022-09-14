import axios from 'axios';

const executeAPI = axios.create({
  baseURL: process.env.REACT_APP_EXECUTE_API_URL_BASE,
});

const dataAPI = axios.create({
  baseURL: process.env.REACT_APP_DATA_API_URL_BASE,
});

export const execute = body => {
  return executeAPI.post('/execute', body);
};

export const getResultsForJob = id => {
  return dataAPI.get(`/responses/job/${id}`);
};
