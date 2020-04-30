package com.wsiz.wirtualny.model.JsonAdapter;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;

public class GradeLecturesList {
    ArrayList<GradeLecturesObiect> lecturesList;

    public GradeLecturesList() {
        lecturesList = new ArrayList<>();
    }

    public void add(GradeLecturesObiect glo) {
        boolean pass = true;

        for (GradeLecturesObiect obiect : lecturesList) {
            if (obiect.nazwaPrzedmiotu.contains(glo.nazwaPrzedmiotu)) {
                pass = false;
                System.out.println("NIE PRZEJDZIESZ!" + glo.nazwaPrzedmiotu);
                if (obiect.t0 < glo.t0 || obiect.t1 < glo.t1 || obiect.t2 < glo.t2 || obiect.t3 < glo.t3) {
                    obiect.setT0(glo.t0);
                    obiect.setT1(glo.t1);
                    obiect.setT2(glo.t2);
                    obiect.setT3(glo.t3);
                }

            }
        }
        if (pass) {
            lecturesList.add(glo);
        }

    }

    public void showList() {
        for (int i = 0; i < lecturesList.size(); i++) {
            System.out.println(lecturesList.get(i).nazwaPrzedmiotu + lecturesList.get(i).t0 + lecturesList.get(i).t1 + lecturesList.get(i).t2 + lecturesList.get(i).t3);
        }
    }

    public ArrayList<GradeLecturesObiect> getLecturesList() {
        return lecturesList;
    }

    public int getGradeCunter() {
        int counter = 0;
        for (GradeLecturesObiect obiect : lecturesList) {
            if (obiect.getT0() != 0 || obiect.getT1() != 0 || obiect.getT2() != 0) {
                counter++;
            }

        }
        return counter;
    }

    public double getGradeAvg() {
        try {
            double avg = 0;
            int count = 0;
            double grade = 0;
            double sum = 0;

            for (GradeLecturesObiect obiect : lecturesList) {

                if (obiect.getT0() != 0 || obiect.getT1() != 0 || obiect.getT2() != 0) {
                    count++;
                    if (obiect.getT0() != 0) {
                        grade = obiect.t0;
                    }
                    if (obiect.getT1() != 0) {
                        grade = obiect.t1;
                    }
                    if (obiect.getT2() != 0) {
                        grade = obiect.t2;
                    }

                    sum = sum + grade;
                }

            }

            if (sum == 0) {
                return 0;
            } else {
                avg = sum / count;

                Double truncatedDouble = BigDecimal.valueOf(avg)
                        .setScale(2, RoundingMode.HALF_UP)
                        .doubleValue();

                return truncatedDouble;

            }
        }catch (ArithmeticException e)
        {
            e.fillInStackTrace();
            return 0;
        }

    }

    public void clearList() {
        lecturesList.clear();
    }
}
