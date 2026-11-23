FROM node:22-alpine

WORKDIR /app

# Copy package files
COPY package*.json ./
RUN npm install

# Copy source files
COPY . .

ENV VITE_API_URL=http://localhost:8081/api

EXPOSE 5173

# Run Vite dev server directly on localhost
CMD ["npm", "run", "dev", "--", "--host"]
