FROM eclipse-temurin:17.0.10_7-jre AS backend_builder
WORKDIR /build/backend
# Start SBT install via Coursier: https://www.scala-sbt.org/1.x/docs/Installing-sbt-on-Linux.html
RUN curl -fLo coursier https://github.com/coursier/launchers/raw/master/coursier \ 
    && chmod +x coursier \
    && ./coursier setup -y
# End
COPY backend/project /build/backend/project
COPY backend/build.sbt /build/backend/
RUN /root/.local/share/coursier/bin/sbt update
COPY backend /build/backend
RUN /root/.local/share/coursier/bin/sbt package \
    && mkdir dist \
    && /root/.local/share/coursier/bin/sbt "export Runtime / fullClasspathAsJars" | tail -n 1 | tr ":" "\n" | xargs -I '{}' mv '{}' /build/backend/dist

FROM node:20.12-slim AS frontend_builder
WORKDIR /build/frontend
COPY frontend/package.json frontend/package-lock.json /build/frontend/
RUN npm install
COPY frontend /build/frontend
RUN npm run build

FROM eclipse-temurin:17.0.10_7-jre
WORKDIR /app/backend
COPY --from=backend_builder /build/backend/dist /app/backend/dist
COPY --from=frontend_builder /build/frontend/dist /app/frontend/dist
ENV TARP_DEV_MODE="false"
EXPOSE 8080
ENTRYPOINT [ "java", "-cp", "/app/backend/dist/*", "Main" ]