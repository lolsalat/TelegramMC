package lolsalat.telgeramMC;

@FunctionalInterface
public interface TriConsumer<S1, S2, S3> {

	public void consume(S1 a, S2 b, S3 c);
	
}
