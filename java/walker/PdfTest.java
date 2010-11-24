package walker;

import java.util.*;

public class PdfTest
{
    public static void main(String args[])
    {
        GraphDB db;

        int arg = 0;
        db = new HibernateGraphDB();

        String category = args[arg];

        /* pick R random ids and run walks on all of them */
        List<Long> ids = db.getRandomNodeIds(10000, category);
        Map<Long,Integer> map = new HashMap<Long,Integer>();

        for (long l : ids)
        {
            Integer before = map.get(l);
            if (before == null)
                before = new Integer(0);
           
            map.put(l, before + 1);
        }

        double sum = 0;
        List<Pdf> pdflist = new ArrayList<Pdf>();
        for (Map.Entry<Long, Integer> e : map.entrySet())
        {
            sum += e.getValue();
            Pdf tmp = new Pdf(e.getKey(), args[0]);
            tmp.setWeight(e.getValue());
            tmp.setSummedWeight(e.getValue());
            pdflist.add(tmp);
        }

        System.out.println("---");

        Collections.sort(pdflist, new Comparator<Pdf>() {
            public int compare(Pdf p1, Pdf p2) {
                return p1.compareTo(p2.getSummedWeight());
            }});
        for (Pdf p : pdflist)
        {
            System.out.println(p.getSource() + " " + 
                               (p.getWeight() / sum));
        }
    }
}
