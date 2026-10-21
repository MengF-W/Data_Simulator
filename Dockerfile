FROM adoptopenjdk/openjdk11:latest

RUN  apt update && apt install -y build-essential pkg-config wget libgl1 libglib2.0-0 libsm6 libxrender1 libxext6 libgtk2.0-dev libxtst6  xorg \
     cmake ant libtbb2 g++ unzip ffmpeg libavformat-dev libavcodec-dev libavutil-dev libswscale-dev libtbb-dev libjpeg-dev libpng-dev libtiff-dev \
     && apt autoremove && apt clean

EXPOSE 500

WORKDIR /application/
COPY target/*.jar ./data_handler/data_handler.jar
COPY target/classes/lib/ ./data_handler/lib/
COPY opencv-4.8.1.zip .
RUN unzip opencv-4.8.1.zip
RUN mkdir -p ./opencv-4.8.1/build

WORKDIR /application/opencv-4.8.1/build
RUN export JAVA_HOME=/usr/lib/jvm/java-11-openjdk-amd64
RUN export ANT_HOME=/usr/share/ant/bin/ant
RUN export ANT_EXECUTABLE=/usr/share/ant/bin/ant
RUN cmake -S /application/opencv-4.8.1 -B . -DBUILD_SHARED_LIBS=OFF -DBUILD_EXAMPLES=OFF -DBUILD_TESTS=OFF -DBUILD_PERF_TESTS=OFF -DBUILD_JAVA=ON -DWITH_V4L=ON -DWITH_FFMPEG=ON -DOPENCV_FFMPEG_SKIP_BUILD_CHECK=ON -DFFMPEG_LIBDIR="/usr/lib64"  -DFFMPEG_INCLUDE_DIRS="/usr/lib64" -DBUILD_opencv_java=ON -DBUILD_opencv_java_bindings_gen=ON -DBUILD_opencv_python_bindings_generator=OFF -DBUILD_opencv_python_tests=OFF -DBUILD_opencv_videoio=ON -DCMAKE_INSTALL_PREFIX=/opt/opencv  > cmake.log
RUN make -j7 > make.log
RUN cd ./lib && mv libopencv_java481.so /application/data_handler/lib/
RUN rm -r /application/opencv-4.8.1

WORKDIR /application/data_handler/
CMD ["java","-Xmx510m","-Xms256M","-XX:PermSize=256m","-XX:MaxPermSize=510m","-Xss510M","-Djava.library.path=/application/data_handler/lib/","-Djavafx.verbose=true","--module-path", "/application/data_handler/lib","--add-modules=javafx.graphics,javafx.controls","-jar", "/application/data_handler/data_handler.jar"]





