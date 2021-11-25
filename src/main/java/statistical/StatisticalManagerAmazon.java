package statistical;

import java.util.ArrayList;

public class StatisticalManagerAmazon extends StatisticalManager {

    public ArrayList<String> dataToPrepare;

    public StatisticalManagerAmazon() {
        dataToPrepare = new ArrayList<>();
        this.preparedDataList = new ArrayList<>();
    }

    @Override
    public void run() {
        prepareAndSetPreparedDataList();
        //punkt 2 - analiza statystyczna
        calculateData();
        //punkt 3 - analiza statystyczna
        identifyRemotePoints();
        //punkt 4 - analiza statystyczna
        calculatePearsonLCC();
        //punkt 6 - analiza statystyczna
        calculatePearsonLCC();

        generateFilesAndCharts();
    }

    @Override
    protected void generateCharts(){
        String tmp = inputPathname;
        if(isPositiveData)
            inputPathname += "/pos";
        else
            inputPathname += "/neg";
        super.generateCharts();
        inputPathname = tmp;
    }

    public void prepareAndSetPreparedDataList(){
        prepareAndSetPreparedDataList(dataToPrepare);
    }

    public void prepareAndSetPreparedDataList(ArrayList<String> dataToPrepare) {
        if(this.preparedDataList == null)
            return;

        DataPreparer dp = DataPreparer.getSimpleInstance(null);
        for(int i = 0; i< dataToPrepare.size();i++){
            PreparedData pd = new PreparedData();
            pd.setContentTotally(dataToPrepare.get(i).split(" "));
            ArrayList<String> importantWords = new ArrayList<>();
            for (String word : pd.getContentTotally())
                if (!dp.getStopWords().contains(word))
                    importantWords.add(dp.deleteSpecialChar(word));

            pd.setImportantContent(importantWords.toArray(new String[0]));
            this.preparedDataList.add(pd);
        }
        this.dataToPrepare = null;
    }

    public void setInputPathname(String inputPathname){
        this.inputPathname = inputPathname;
    }

    //TODO if needed
    @Override
    protected void generateRaport() {
        System.out.println("Generowanie plikow dla zbioru 'amazon' nie ma implementacji.");
    }
}
