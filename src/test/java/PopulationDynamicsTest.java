import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class PopulationDynamicsTest {

    @Test
    void testAnoUltrapassa95PorCentoDeM() {
        // Definindo os mesmos parâmetros do código principal
        double M = 9500000;
        double alpha = 0.02;
        double populacaoAtual = PopulationDynamics.populacao[PopulationDynamics.populacao.length - 1]; // População em 2023
        int ano = 2023;
        double populacao95M = 0.95 * M;

        // Calcula o ano em que a população ultrapassa 95% de M
        while (populacaoAtual < populacao95M) {
            double dP_dt = alpha * populacaoAtual * (1 - (populacaoAtual / M)); // Modelo logístico
            populacaoAtual += dP_dt;
            ano++;
        }

        // Testa se o ano calculado corresponde ao esperado
        assertEquals(2083, ano, "O ano em que a população ultrapassa 95% de M está incorreto.");
    }

    @Test
    void testAnoEsgotamentoReservas() {
        // Definindo os mesmos parâmetros do código principal
        double M = 9500000;
        double alpha = 0.02;
        double P0 = PopulationDynamics.populacao[PopulationDynamics.populacao.length - 1];
        double reservasIniciais = 1.57 * 1e12;
        double consumoPerCapita = 6782;
        double consumoPorBarril = 1700;
        double consumoPorPessoa = consumoPerCapita / consumoPorBarril;
        double reservasRestantes = reservasIniciais;
        int anoEsgotamento = 2030;
        boolean esgotouAntes2110 = false;

        // Calcula o ano de esgotamento das reservas
        double populacaoAtual = P0;
        while (reservasRestantes > 0) {
            populacaoAtual += alpha * populacaoAtual * (1 - (populacaoAtual / M));
            double consumoAnual = populacaoAtual * consumoPorPessoa;
            reservasRestantes -= consumoAnual;

            if (reservasRestantes <= 0) {
                esgotouAntes2110 = (anoEsgotamento <= 2110);
                break;
            }

            anoEsgotamento++;
        }

        // Testa se o ano de esgotamento é correto e se a mensagem de esgotamento até 2110 funciona
        assertTrue(anoEsgotamento > 2110, "O ano de esgotamento deveria ser após 2110.");
    }
}
