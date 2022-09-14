# Ensembler ML-worker template dockerfile
FROM ubuntu

# 2. Inject ensembler base worker msvc
ADD worker-install.sh .
RUN chmod +x /worker-install.sh
RUN /worker-install.sh
WORKDIR /usr/src/ensembler
COPY worker-0.0.1.jar .
ENTRYPOINT ["java","-jar","worker-0.0.1.jar"]

# 4. ----- BEGIN INSTRUCTIONS -----
# Your dockerfile instructions
ADD install.sh .
RUN chmod +x install.sh
RUN ./install.sh
COPY requirements.txt .
RUN pip install --no-cache-dir --upgrade pip \
    && pip install --no-cache-dir -r requirements.txt
COPY . .

# 4. ----- END OF INSTRUCTIONS -----
# Do not overwrite ENTRYPOINT command! 
# Your ML app will be executed by the ensembler worker app.
# Execute commands will be passed via ensembler's REST API.
