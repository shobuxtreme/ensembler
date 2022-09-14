FROM node:14-alpine

# set working directory
WORKDIR /usr/src/ensembler

ENV PATH /usr/src/ensembler/node_modules/.bin:$PATH
ENV API_EXECUTOR = "http://34.65.33.166:9000"
ENV API_DATA="http://34.65.33.166:9002"

# install app dependencies
COPY package.json ./
COPY package-lock.json ./
RUN npm install --silent

# add app
COPY . ./

# start app
ENTRYPOINT REACT_APP_EXECUTE_API_URL_BASE=${API_EXECUTOR} REACT_APP_DATA_API_URL_BASE=${API_DATA} npm start