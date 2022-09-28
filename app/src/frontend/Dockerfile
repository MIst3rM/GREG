FROM node:18.9.0

COPY package.json yarn.lock ./

RUN yarn install

# Start application
CMD ["yarn", "dev"]