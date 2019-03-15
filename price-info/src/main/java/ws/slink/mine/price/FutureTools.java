package ws.slink.mine.price;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

public class FutureTools {

    private static final Logger logger = LoggerFactory.getLogger(FutureTools.class);


    public static <T> CompletableFuture<List<T>> allFutures(List<CompletableFuture<T>> futures) {
        return CompletableFuture.allOf(futures.toArray(new CompletableFuture[futures.size()]))
                .thenApply(v -> futures.stream()
                        .map(CompletableFuture::join)
                        .collect(Collectors.toList()));
    }

    public static <T> List<T> all(List<CompletableFuture<Optional<T>>> futures) {
        try {
            return
              allFutures(futures).get()
                                 .stream()
                                 .filter(o -> o.isPresent())
                                 .map(o -> o.get())
                                 .collect(Collectors.toList());
        } catch (InterruptedException e) {
            logger.warn("WalletUpdateTask interrupt: " + e.getMessage());
        } catch (ExecutionException e) {
            logger.warn("WalletUpdateTask error: " + e.getMessage());
        }
        return Collections.EMPTY_LIST;
    }

}
