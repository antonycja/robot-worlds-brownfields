# syntax=docker/dockerfile:1
#just a skeleton I want to Pull from HOME
FROM jdk:11
WORKDIR /robot_worlds_dbn_13
COPY . .
RUN yarn install --production
CMD ["node", "src/index.js"]
EXPOSE 3000