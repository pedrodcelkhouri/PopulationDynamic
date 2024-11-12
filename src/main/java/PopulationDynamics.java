import java.util.ArrayList;
import java.util.List;

public class PopulationDynamics {

    // Dados da população e crescimento
    static int[] anos = {1950, 1951, 1952, 1953, 1954, 1955, 1956, 1957, 1958, 1959,
            1960, 1961, 1962, 1963, 1964, 1965, 1966, 1967, 1968, 1969,
            1970, 1971, 1972, 1973, 1974, 1975, 1976, 1977, 1978, 1979,
            1980, 1981, 1982, 1983, 1984, 1985, 1986, 1987, 1988, 1989,
            1990, 1991, 1992, 1993, 1994, 1995, 1996, 1997, 1998, 1999,
            2000, 2001, 2002, 2003, 2004, 2005, 2006, 2007, 2008, 2009,
            2010, 2011, 2012, 2013, 2014, 2015, 2016, 2017, 2018, 2019,
            2020, 2021, 2022, 2023};
    static double[] populacao = {2493093, 2536927, 2584086, 2634106, 2685895, 2740214, 2795410, 2852618, 2911250, 2965950,
            3015471, 3064870, 3123374, 3192808, 3264487, 3334534, 3404041, 3473413, 3545187, 3619492,
            3694684, 3769848, 3844918, 3920805, 3996416, 4070735, 4144246, 4217864, 4292098, 4368540,
            4447606, 4528777, 4612673, 4697328, 4782176, 4868943, 4958073, 5049746, 5141993, 5234432,
            5327803, 5418736, 5505990, 5591545, 5675551, 5758879, 5842056, 5924788, 6007067, 6089006,
            6171703, 6254936, 6337730, 6420362, 6503378, 6586970, 6671452, 6757309, 6844458, 6932766,
            7021732, 7110924, 7201202, 7291794, 7381616, 7470492, 7558555, 7645618, 7729903, 7811294,
            7887001, 7954448, 8021407, 8091735};

    public static void main(String[] args) {
        // Parâmetros iniciais para o modelo logístico
        double M = 9500000; // Capacidade máxima aproximada
        double alpha = 0.02; // Taxa de crescimento aproximada
        double P0 = populacao[populacao.length - 1]; // População inicial (2023)
        int anoInicial = 2023;

        // Parte 1: Geração da Tabela de População e dP/dt
        List<String[]> tabelaPopulacao = new ArrayList<>();
        tabelaPopulacao.add(new String[]{"Ano", "População", "dP/dt"});

        for (int i = 1; i < anos.length; i++) {
            double dP = (populacao[i] - populacao[i - 1]); // Variação de P
            double dt = anos[i] - anos[i - 1];
            double taxaVariacao = dP / dt;
            tabelaPopulacao.add(new String[]{String.valueOf(anos[i]), String.format("%.0f", populacao[i]), String.format("%.2f", taxaVariacao)});
        }

        // Exibição da tabela populacional
        System.out.println("Tabela de População e Taxa de Variação");
        for (String[] linha : tabelaPopulacao) {
            System.out.printf("%-5s %-12s %-10s\n", linha[0], linha[1], linha[2]);
        }

        // Parte 2: Projeção da População Ano a Ano até atingir 95% de M
        double populacaoAtual = P0;
        int ano = anoInicial;
        double populacao95M = 0.95 * M;

        while (populacaoAtual < populacao95M) {
            double dP_dt = alpha * populacaoAtual * (1 - (populacaoAtual / M)); // Modelo logístico
            populacaoAtual += dP_dt; // Atualiza a população para o próximo ano
            ano++;
        }

        System.out.println("Ano em que a população ultrapassará 95% de M: " + ano);

        // Parte 3: Projeção de Consumo de Petróleo e Esgotamento
        double reservasIniciais = 1.57 * 1e12; // Barris
        double consumoPerCapita = 6782; // kWh por pessoa por ano
        double consumoPorBarril = 1700; // kWh por barril (aproximado)
        double consumoPorPessoa = consumoPerCapita / consumoPorBarril; // Consumo em barris por pessoa
        double reservasRestantes = reservasIniciais;
        int anoEsgotamento = 2030;
        boolean esgotouAntes2110 = false;

        // Integração numérica para calcular o ano de esgotamento das reservas
        populacaoAtual = P0;

        while (reservasRestantes > 0) {
            populacaoAtual += alpha * populacaoAtual * (1 - (populacaoAtual / M)); // Crescimento populacional ano a ano
            double consumoAnual = populacaoAtual * consumoPorPessoa;
            reservasRestantes -= consumoAnual;

            // Imprime apenas os anos múltiplos de 10 até 2110
            if (anoEsgotamento <= 2110 && anoEsgotamento % 10 == 0) {
                System.out.printf("Ano: %d, População: %.0f, Consumo Anual: %.2f, Reservas Restantes: %.2f\n",
                        anoEsgotamento, populacaoAtual, consumoAnual, reservasRestantes);
            }

            if (reservasRestantes <= 0) {
                esgotouAntes2110 = (anoEsgotamento <= 2110);
                break;
            }

            anoEsgotamento++;
        }

        // Mensagem caso as reservas não se esgotem até 2110
        if (!esgotouAntes2110 && anoEsgotamento > 2110) {
            System.out.println("As reservas não se esgotaram até 2110 dentro de uma precisão de 10%.");
        }

        // Imprime o ano exato de esgotamento das reservas
        System.out.println("Ano exato estimado de esgotamento das reservas de petróleo: " + anoEsgotamento);
    }
}
