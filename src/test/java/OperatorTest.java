import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.Arrays;

public class OperatorTest {

    @Test
    public void testMap() {
        Flux.fromIterable(Arrays.asList("A", "B", "C")).map(String::toLowerCase)
                .subscribe(System.out::println);
    }

    @Test
    public void testMapWithRange() {
        Flux.range(1, 5)
                .flatMap(s -> Flux.range(s * 5, 2))
                .subscribe(System.out::println);
    }

    @Test
    public void testConvertMonoIntoFlux() {
        Mono.just(9)
                .flatMapMany(i -> Flux.range(5, i))
                .subscribe(System.out::println);
    }

    @Test
    public void testFluxConcat() throws InterruptedException {
        Flux first = Flux.range(1, 5).delayElements(Duration.ofMillis(400));
        Flux second = Flux.range(6, 5).delayElements(Duration.ofMillis(500));

        first.concatWith(second).subscribe(System.out::println);

        Thread.sleep(5000);

    }

    @Test
    public void testFluxMerge() throws InterruptedException {
        Flux first = Flux.range(1, 5).delayElements(Duration.ofMillis(400));
        Flux second = Flux.range(6, 5).delayElements(Duration.ofMillis(500));

        first.mergeWith(second).subscribe(System.out::println);

        Thread.sleep(5000);

    }


    @Test
    public void testFluxZip() throws InterruptedException {
        Flux first = Flux.range(1, 5);
        Flux second = Flux.range(6, 5);

        first.zipWith(second, (i1,i2) -> i1 +" + "+ i2 + " = "+ ((int)i1+(int)i2)).subscribe(System.out::println);


    }
}