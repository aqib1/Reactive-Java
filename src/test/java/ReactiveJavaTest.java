import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;

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

}
