# Respostas — Sistema de Reserva de Laboratorio

Nome completo: Caio Henrique dos Santos  RA: ______________

> Preencha **todos** os campos. Campo vazio é campo não avaliado.
> Não apague os títulos nem os rótulos em negrito: a correção procura por eles.

---

# Etapa 1 — Diagnóstico

## Problema 1 — a conversão do inventario patrimonial

**Em que consiste o problema, e o que acontece quando o sistema cresce:** Ao analisar o código, percebi que o serviço InventarioLegadoWS devolve os equipamentos em uma matriz String, trazendo a situação codificada em uma letra ("D" ou "B", que às vezes vem em minúscula). O problema é que a conversão desse formato para objetos Equipamento foi duplicada em dois lugares do sistema e as duas implementações já estão conflitando: no catálogo só se reconhece a letra maiúscula ("D".equals(...)), enquanto no relatório a verificação ignora a diferença entre maiúsculas e minúsculas ("D".equalsIgnoreCase(...)). Por causa disso, o equipamento PAT-1003 aparece como indisponível no catálogo e como disponível no relatório. Se o sistema crescer e surgirem novas classes precisando consultar o acervo, ou se a estrutura da matriz mudar, eu teria que repetir e dar manutenção nessa mesma conversão em vários pontos diferentes, o que aumenta muito o risco de erros.


**Princípio SOLID violado:** Identifiquei a violação do Princípio da Inversão de Dependência, pois as classes consumidoras dependem diretamente da classe concreta do serviço legado em vez de dependerem de uma abstração e também a violação do Princípio da Responsabilidade Única, já que cada classe acumula sua função principal com a tarefa de traduzir o formato bruto da matriz.


**Classes afetadas (pelo nome):** CatalogoEquipamentos, RelatorioAcervo e o Main, que instancia o serviço legado e o injeta nessas duas classes, consumindo InventarioLegadoWS.


**Que outra dependência indevida essas classes têm em relação ao inventario patrimonial, e por que ela é um problema:** Além de duplicarem a conversão, as duas classes dependem diretamente da classe concreta InventarioLegadoWS e conhecem detalhes internos da matriz que retorna, como a posição exata das colunas de código, descrição e situação. Entendo que isso é um problema porque cria um acoplamento forte: se eu precisar substituir esse serviço legado por outra fonte de dados, vou ser obrigado a alterar todas as classes que consomem o inventário.


## Problema 2 — o ciclo de vida de Reserva

**Em que consiste o problema, e o que acontece quando o sistema cresce:** Na classe Reserva, vi que o controle das fases do ciclo de vida (SOLICITADA, EM_USO e ENCERRADA) é feito por meio de um campo String e de condicionais if/else espalhadas pelos métodos públicos retirar(), devolver() e cancelar(). Desse jeito, eu não consigo ler o comportamento completo de cada fase em um único lugar, e ainda corro o risco de um erro de digitação no texto da situação passar despercebido na compilação e só falhar em tempo de execução. Quando o sistema cresce, se for necessário adicionar um novo estado ou uma nova ação, eu teria que alterar todos os métodos públicos da classe, deixando a manutenção mais trabalhosa e propensa a quebrar regras que já funcionavam.


**Princípio SOLID violado:** Identifiquei a violação do Princípio Aberto/Fechado (OCP), porque a classe não permite estender o ciclo de vida com novas fases sem exigir alterações diretas no código existente dos seus métodos. Também considero que o Princípio da Responsabilidade Única (SRP) fica comprometido, pois Reserva concentra seus próprios dados junto com todas as regras de transição de todas as fases.


**Classes afetadas (pelo nome):** Reserva


---

# Etapa 4 — Justificativa

## Padrão aplicado ao Problema 1

**O problema que ele resolve (descreva antes de nomear o padrão):** Permite integrar uma classe externa com interface incompatível a um sistema que espera trabalhar com objetos de domínio (List<Equipamento>), sem modificar o componente externo, centralizando a conversão de formato em um único ponto e eliminando a dependência direta das classes consumidoras sobre a classe concreta legada.


**Nome do padrão:** Adapter


**Alternativa descartada e por que ela não resolveria por completo:** Alterar o serviço legado (InventarioLegadoWS). Essa opção é inviável porque o serviço é mantido por outra equipe e o código-fonte está fora do controle do nosso sistema. Então mesmo que apenas se corrigisse a letra minúscula na origem, CatalogoEquipamentos e RelatorioAcervo continuariam acoplados à classe concreta e duplicando a lógica de conversão da matriz para objetos


## Padrão aplicado ao Problema 2

**O problema que ele resolve (descreva antes de nomear o padrão):** Permite que um objeto altere seu comportamento conforme muda sua fase interna, isolando as regras de operação e transição de cada fase em classes próprias, eliminando condicionais espalhadas pelos métodos públicos e tornando o ciclo de vida legível e extensível sem alterar os métodos da classe principal.


**Nome do padrão:** State


**Alternativa descartada e por que ela não resolveria por completo:** Trocar a String por um enum. Um enum simples evitaria erros de digitação em tempo de compilação, mas não resolveria o problema central: as condicionais (if/else ou switch) continuariam espalhadas pelos métodos retirar(), devolver() e cancelar() de Reserva, mantendo a violação ao OCP e impedindo a leitura isolada de cada fase em sua própria classe.
