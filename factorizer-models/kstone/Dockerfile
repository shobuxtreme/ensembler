# Ensembler ML-worker template dockerfile
FROM ubuntu

# 2. Inject ensembler base worker msvc
ADD factorizer-install.sh .
RUN chmod +x /factorizer-install.sh
RUN /factorizer-install.sh
WORKDIR /usr/src/ensembler
COPY factorizer-0.0.1.jar .
ENTRYPOINT ["java","-jar","factorizer-0.0.1.jar"]

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
