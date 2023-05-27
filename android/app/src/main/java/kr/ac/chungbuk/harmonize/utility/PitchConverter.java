package kr.ac.chungbuk.harmonize.utility;

import java.util.Arrays;

public class PitchConverter {

    private static final double[] pitchLevel = new double[] {
        0.0, 0.11, 0.14, 0.17, 0.19, 0.22, 0.25, 0.28, 0.30, 0.33, 0.36, 0.38, 0.41, 0.44, 0.47, 0.49,
        0.52, 0.55, 0.57, 0.60, 0.63, 0.66, 0.68, 0.71, 0.74, 0.76, 0.79, 0.82, 0.85, 0.87, 0.90, 1.0
    };

    private static final String[] pitchString = new String[] {
        "C2", "C2", "D2", "E2", "F2", "G2", "A2", "B2", "C3", "D3", "E3", "F3", "G3", "A3", "B3",
        "C4", "D4", "E4", "F4", "G4", "A4", "B4", "C5", "D5", "E5", "F5", "G5", "A5", "B5", "C6", "D6", "D6"
    };


    /**
     * @param value MusicDetail에서 받은 음높이 max, min 값
     * @return C3 D3 등 음계 문자열
     */
    public static String doubleToPitch(double value)
    {
        int nearestIndex = searchNearest(value);

        if (0 <= nearestIndex && nearestIndex < 32)
            return pitchString[nearestIndex];
        else
            return pitchString[0];
    }

    private static int searchNearest(double value) {
        int result = Arrays.binarySearch(pitchLevel, value);
        if (result >= 0) { return result; }

        int insertionPoint = -result - 1;
        return (pitchLevel[insertionPoint] - value) < (value - pitchLevel[insertionPoint - 1]) ?
                insertionPoint : insertionPoint - 1;
    }

}
