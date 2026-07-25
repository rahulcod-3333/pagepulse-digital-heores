# ---------- Stage 1: Build ----------
# Node is only needed to install deps and run the Vite/CRA build —
# discarded from the final image.
FROM node:20-alpine AS build

WORKDIR /app

# Copy package files first so `npm install` is cached separately
# from source code changes.
COPY package.json package-lock.json* ./
RUN npm ci

COPY . .

# Bake the backend URL in at build time. Override with:
#   docker build --build-arg VITE_API_BASE_URL=https://your-api.com .
# Defaults to localhost for a purely local docker run.
ARG VITE_API_BASE_URL=http://localhost:8080
ENV VITE_API_BASE_URL=$VITE_API_BASE_URL

RUN npm run build

# ---------- Stage 2: Serve ----------
# A tiny nginx image just to serve the static build output —
# no Node, no npm, nothing but the compiled files + a web server.
FROM nginx:alpine

# SPA routing: any unmatched path falls back to index.html so
# client-side routing (if you add any) doesn't 404 on refresh.
COPY nginx.conf /etc/nginx/conf.d/default.conf

COPY --from=build /app/dist /usr/share/nginx/html

EXPOSE 80

CMD ["nginx", "-g", "daemon off;"]
