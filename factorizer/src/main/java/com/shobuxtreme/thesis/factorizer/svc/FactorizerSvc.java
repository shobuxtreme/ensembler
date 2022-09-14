package com.shobuxtreme.thesis.factorizer.svc;

import com.shobuxtreme.thesis.core.DataRequest;
import com.shobuxtreme.thesis.core.ExecuteKind;
import org.apache.commons.io.IOUtils;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.io.*;

@Component
@Configuration
@PropertySource("classpath:application.properties")
public class FactorizerSvc {

    public DataRequest factorize(final DataRequest request) {
        final ExecuteKind kind = request.getKind();
        System.out.println(request);

        switch (kind) {
            case TRAIN -> {
                return executeRuntime(request, "train.csv");
            }
            case VALIDATE -> {
                return provideStaticResource(request, "validate.csv");
            }

            case PREDICT -> {
                return provideStaticResource(request, "predict.csv");
            }
        }

        request.setResult(-1);
        request.setResultMsg("Invalid request");
        return request;
    }

    private DataRequest executeRuntime(final DataRequest request, final String fileName) {
        try {
            // Factorize
            final String execCmd = request.getFactorizeCmd();
            Runtime runTime = Runtime.getRuntime();
            Process process = runTime.exec(execCmd);
            InputStream inputStream = process.getInputStream();
            InputStreamReader isr = new InputStreamReader(inputStream);
            int n1;
            char[] c1 = new char[1024];
            StringBuilder standardOutput = new StringBuilder();
            while ((n1 = isr.read(c1)) > 0) {
                standardOutput.append(c1, 0, n1);
            }
            process.waitFor();
            final int exitVal = process.exitValue();
            if (exitVal != 0){
                request.setResult(exitVal);
                request.setResultMsg(standardOutput.toString());
                process.destroy();
                return request;
            }
            request.setResult(exitVal);
            request.setResultMsg(standardOutput.toString());
            process.destroy();

            // Read CSV
            InputStream fileInputStream = new FileInputStream(fileName);
            byte[] byteArray = IOUtils.toByteArray(fileInputStream);
            request.setData(byteArray);

        }catch (Exception e){
            e.printStackTrace();
            request.setResult(-1);
            request.setResultMsg(e.toString());
        }
        return request;
    }

    private DataRequest provideStaticResource(final DataRequest request, final String fileName) {
        try {
            InputStream fileInputStream = new FileInputStream(fileName);
            byte[] byteArray = IOUtils.toByteArray(fileInputStream);
            request.setData(byteArray);
            request.setResult(0);
            request.setResultMsg(fileName + " data added");
            return request;
        } catch (Exception e ){
            e.printStackTrace();
            request.setResult(-1);
            request.setResultMsg(e.toString());
        }
        return request;
    }
}
