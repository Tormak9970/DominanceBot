package Dominance.util;

import java.io.*;
import java.util.ArrayList;

public class LUT{

    public static final AccelModel[] accelTable = createLUT("acceleration.txt");
    public static final AccelModel[] boostAccelTable = createLUT("boost_acceleration.txt");
    public static AccelModel[] createLUT(String fileName){
        ArrayList<AccelModel> lut = new ArrayList<>();

        try {
            InputStream in = LUT.class.getClassLoader().getResourceAsStream(fileName);
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            String line = reader.readLine();
            while (line != null) {
                lut.add(AccelModel.fromString(line));
                // read next line
                line = reader.readLine();
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NullPointerException e){
            e.printStackTrace();
            System.out.println("fileNotFound");
            System.out.println(fileName);
        }

        AccelModel[] arr = new AccelModel[lut.size()];
        arr = lut.toArray(arr);
        return arr;
    }

    private static AccelModel getVelocityRecursive(AccelModel[] accelModels, double vel, int s, int e) {
        int l = e - s;
        int div = s + (l / 2);
        if (l == 1){
            return accelModels[div];
        }else if(accelModels[div].AccelModelVel > vel){
            return getVelocityRecursive(accelModels, vel, s, div);
        }else{
            return getVelocityRecursive(accelModels, vel, div, e);
        }
    }

    public static AccelModel getVelocity(AccelModel[] accelModels, double vel){
        return getVelocityRecursive(accelModels, vel, 0, accelModels.length);
    }

    private static AccelModel getPositionRecursive(AccelModel[] accelModels, double pos, int s, int e) {
        int l = e - s;
        int div = s + (l / 2);
        if (l == 1){
            return accelModels[div];
        }else if(accelModels[div].AccelModelPos > pos){
            return getVelocityRecursive(accelModels, pos, s, div);
        }else{
            return getVelocityRecursive(accelModels, pos, div, e);
        }
    }

    public static AccelModel getPosition(AccelModel[] accelModels, double pos){
        return getPositionRecursive(accelModels, pos, 0, accelModels.length);
    }

    private static AccelModel getTimeRecursive(AccelModel[] accelModels, double time, int s, int e) {
        int l = e - s;
        int div = s + (l / 2);
        if (l == 1){
            return accelModels[div];
        }else if(accelModels[div].AccelModelTime > time){
            return getTimeRecursive(accelModels, time, s, div);
        }else{
            return getTimeRecursive(accelModels, time, div, e);
        }
    }

    public static AccelModel getTime(AccelModel[] accelModels, double time){
        return getTimeRecursive(accelModels, time, 0, accelModels.length);
    }

}
