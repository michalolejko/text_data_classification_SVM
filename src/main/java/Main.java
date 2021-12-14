import explore.ExploreManager;
import org.tensorflow.Signature;
import org.tensorflow.op.Ops;
import org.tensorflow.op.core.Placeholder;
import org.tensorflow.op.math.Add;
import org.tensorflow.types.TInt32;
import others.MainManagement;
import others.Menu;
import others.MyStopwatch;
import statistical.StatisticalManager;

public class Main {

    public static final int maxLinesToBeProcessedStatistical = 100;
    public static StatisticalManager[] statisticalManagerAcllmbds;
    public static StatisticalManager[] statisticalManagerAmazon;

    public static void main(String[] args) throws Exception {
        MyStopwatch stopwatch = new MyStopwatch();

        //Menu.start(maxLinesToBeProcessedStatistical);

        statisticalManagerAcllmbds = MainManagement.calculateStatisticalDataAcllmbd(maxLinesToBeProcessedStatistical, false, false);
        statisticalManagerAmazon = MainManagement.calculateStatisticalDataAmazon(maxLinesToBeProcessedStatistical, false, false);
        
        MainManagement.runExplore(statisticalManagerAcllmbds);
        MainManagement.runExplore(statisticalManagerAmazon);

        System.out.println(stopwatch);

//        System.out.println("Hello TensorFlow " + TensorFlow.version());
//
//        try (ConcreteFunction dbl = ConcreteFunction.create(Main::dbl);
//             TInt32 x = TInt32.scalarOf(10);
//             Tensor dblX = dbl.call(x)) {
//            System.out.println(x.getInt() + " doubled is " + ((TInt32)dblX).getInt());
//        }
    }

    private static Signature dbl(Ops tf) {
        Placeholder<TInt32> x = tf.placeholder(TInt32.class);
        Add<TInt32> dblX = tf.math.add(x, x);
        return Signature.builder().input("x", x).output("dbl", dblX).build();
    }

}
