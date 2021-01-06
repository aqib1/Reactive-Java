import org.junit.jupiter.api.Test;
import org.reactivestreams.Subscription;
import reactor.core.publisher.BaseSubscriber;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.Arrays;
import java.util.Random;

public class ReactiveJavaTest {

    @Test
    public void testJustMono(){
        Mono.just("22")
                .log().subscribe();
    }

    @Test
    public void testJustMonoWithSubscribeConsumer(){
        Mono.just(1).log().subscribe(x-> System.out.println(x*23));
    }

    @Test
    public void testMonoEvents() {
        Mono.just("A")
                .log()
                .doOnSubscribe(System.out::println)
                .doOnRequest(System.out::println)
                .doOnNext(x->System.out.println("OnNext"))
                .doOnCancel(()-> System.out.println("OnCancel"))
                .subscribe(System.out::println);
    }

    @Test
    public void testMonoRuntimeError() {
        Mono.error(new RuntimeException())
                .log()
                .subscribe();
    }

    @Test
    public void testMonoExceptionAndRethrow() {
        Mono.error(new RuntimeException()).log()
                .subscribe(System.out::println, e-> System.out.println("Err : "+e));
    }

    @Test
    public void testMonoExceptionDoOnError() {
        Mono.error(new RuntimeException()).log()
        .doOnError(e-> System.out.println("ERROR : "+e))
        .subscribe();
    }

    @Test
    public void testMonoExceptionCaught() {
            Mono.error(new RuntimeException())
                    .log()
                    .onErrorResume(e -> {
                        System.out.println("Error" + e);
                        return Mono.just("A");
                    }).subscribe(System.out::println);
    }

    @Test
    public void testOnErrorReturn() {
        Mono.error(new Exception())
                .onErrorReturn("A")
                .log()
                .subscribe(System.out::println);
    }


    @Test
    public void fluxJustTest() {
        String [] test = {"A","B","C"};
        Flux.just(test)
                .map(String::toLowerCase)
                .log()
                .subscribe(System.out::println);
    }

    @Test
    public void fluxFromIterator() {
        Flux.just(Arrays.asList("A","B","C"))
                .log().subscribe();

        Flux.fromIterable(Arrays.asList("A","B","C"))
                .map(String::toLowerCase)
                .log()
                .subscribe();
    }

    @Test
    public void fluxFromRange() {
        Flux.range(8, 10)
                .log().subscribe();
    }


    //Take method does not work as backpressure
    // it just take N values and terminate process
    @Test
    public void fluxRangeAndTake() throws InterruptedException {
        Flux.interval(Duration.ofSeconds(1))
        .log()
        .take(3)
        .subscribe();
        Thread.sleep(5000);
    }

    @Test
    public void fluxBackPressure() {
        Flux.range(3, 10)
                .log()
                .subscribe(null, null, null,
                        s->s.request(3));
    }

    @Test
    public void fluxWithComplexBackPressure() {
        Flux.range(3, 10)
        .log()
        .subscribe(new BaseSubscriber<Integer>() {
            int elementsToProcess = 3;
            int count = 0;
            @Override
            protected void hookOnSubscribe(Subscription subscription) {
                request(elementsToProcess);
            }

            @Override
            protected void hookOnNext(Integer value) {
                super.hookOnNext(value);
                System.out.println(value);
                count++;
                if (count == elementsToProcess) {
                    count = 0;

                    Random r = new Random();
                    elementsToProcess = r.ints(1, 4).findFirst().getAsInt();
                    request(elementsToProcess);
                }
            }
        });
    }

}
