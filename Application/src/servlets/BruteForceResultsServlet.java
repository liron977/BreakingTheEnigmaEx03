package servlets;

import bruteForce.DecryptionInfoDTO;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import constants.ParametersConstants;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import managers.BruteForceResultsInfoManager;
import utils.ServletUtils;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BruteForceResultsServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try (PrintWriter out = response.getWriter()) {
            response.setContentType("application/json");
            BruteForceResultsInfoManager bruteForceResultsInfoManager = ServletUtils.getBruteForceResultsInfoManager(getServletContext());
            Map<String, List<DecryptionInfoDTO>> bruteForceResultsInfoManagerMap = bruteForceResultsInfoManager.getBruteForceResultsMap();
            String theBattleFieldName = request.getParameter(ParametersConstants.BATTLE_FIELD);
            List<DecryptionInfoDTO> decryptionInfoDTOList=createDecryptionInfoDTOList(bruteForceResultsInfoManagerMap,theBattleFieldName);
            if (decryptionInfoDTOList != null) {

                Gson gson = new Gson();
                String json = gson.toJson(decryptionInfoDTOList);
                out.println(json);
                out.flush();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
        @Override
        protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
            BruteForceResultsInfoManager bruteForceResultsInfoManager = ServletUtils.getBruteForceResultsInfoManager(getServletContext());
            String theBattleFieldName = request.getParameter(ParametersConstants.BATTLE_FIELD);
            Gson gson= new Gson();
            Type listDecryptionInfoDTO = new TypeToken<ArrayList<DecryptionInfoDTO>>() {}.getType();
            List<DecryptionInfoDTO> dtoFromGson=gson.fromJson(request.getReader(),listDecryptionInfoDTO);
            bruteForceResultsInfoManager.addBruteForceResult(theBattleFieldName,dtoFromGson);
        }
        private List<DecryptionInfoDTO> createDecryptionInfoDTOList( Map<String, List<DecryptionInfoDTO>> bruteForceResultsInfoManagerMap, String theBattleFieldName){
        List<DecryptionInfoDTO> decryptionInfoDTOList=new ArrayList<>();
            decryptionInfoDTOList= bruteForceResultsInfoManagerMap.get(theBattleFieldName);
            return decryptionInfoDTOList;

        }

    }