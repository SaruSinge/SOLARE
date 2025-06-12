Descrição do projeto:
	Esse projeto foi feito para ser uma aplicação para registro e organização de tarefas. Cada usuário terá uma conta individual, a qual lhe confere o acesso, uma vez cadastrado, e permite que ele crie, veja, edite e delete o que aqui chamamos de eventos, podendo ser desde tarefas, a compromissos e até registros para controle de operação. 
	Nosso objetivo é permitir que o usuário possa interagir com o sistema, o qual deve ser claro e conciso. A partir disso, desenvolvemos as seguintes estipulações:

Para login, cadastro e autentificação:
O usuário deve entrar com um NOME, um EMAIL e uma SENHA. Para que possa se autentificar, esses dados devem ser registrados no banco de dados. Por isso, deve haver uma tabela USUARIO. Essa tabela deve armazenar essas informações, mas, também deve identificá-las separadamente, o que gera a necessidade de uma chave primária. NOME e SENHA podem ser repetidos, EMAIL só poder ter um do tipo por vez em utilização, porém, o usuário pode querer mudar o EMAIL para fins organizacionais pessoais, o que torna o dado EMAIL mutável. Para resolver isso, deve haver um dado ID gerado automaticamente que será atribuído a cada conjunto de dados, que chamaremos de CADASTRO.
Ao entrar na aplicação deve haver as opções de ENTRAR e CADASTRAR, presumindo que o usuário pode ou não ter uma conta.  Caso ele clique na opção errada ou se depare com a necessidade de mudar para outra página, deve haver uma opção de volta ou mudança para a página desejada dentro da página atual em que ele está até o momento de identificação de usuário.
O programa deve, quando um cliente se cadastrar, salvar e retornar o ID do cliente para o resto do programa se manter ciente qual o usuário que está logado e futuramente responder os comandos de acordo com o que está relacionado a essa conta. Já a autentificação deve ser feita a base da comparação de EMAIL e SENHA. Como o EMAIL é único (mesmo que possa ser alterado) ele servirá para identificar a conta no momento de login, e a SENHA será o que confirmará a identidade do usuário. Analisados e confirmados a congruência dos dados, ele permitirá o acesso ao programa.

Dashboard/tela principal:
	Uma vez confirmada e identificada a identidade do usuário, a tela principal exibirá as opções/ações que usuário pode acionar no programa dentro de duas regiões: MENU e BOTÕES DE EVENTO. O MENU contará com o botão de SAIR que fecha o programa e reabre a TELA INICIAL, onde há as opções para entrar ou se cadastrar. Tem também o botão SEUS DADOS, o qual serve para redirecioná-lo para uma página com os dados de CADASTRO, e porventura editá-los, ou encerrar a conta, apagando seus dados de CADASTRO e todos os registros feitos por essa conta. O usuário deve conseguir voltar por um botão à DASHBOARD.
	No conjunto BOTÕES DE EVENTO, deve haver as funcionalidades de CRUD (Create, Read, Update, Delete / Criar, Ler, Atualizar, Deletar). Terá que se ter um botão CRIAR EVENTO, o qual disponibiliza uma página para a criação de um EVENTO, o qual consistira dos seguintes campos e tipos:
- Nome, de tipo texto aceitando qualquer caractere.
- Marcador/Lista, de tipo texto aceitando qualquer caractere.
- Descrição, de tipo texto aceitando qualquer caractere.
- Data, de tipo data, em dias, meses e anos.
- Horário, do tipo de marcação de tempo em hora e minutos.
- Local, de tipo texto aceitando qualquer caractere.
- Data de Criação, que marque o dia e a hora em que foi feita.
- Data de Modificação, que marque o dia e a hora em que foi modificada.
	Abaixo deve haver opções para SALVAR EVENTO e VOLTAR, considerando que o usuário possa querer apenas uma das opções abaixo enquanto ainda esta nessa página. Para fins de identificação e funcionalidade, cada EVENTO deve receber um id próprio e deve armazenar o ID do usuário que o fez, o mesmo ID que ele deve receber da confirmação do login/CADASTRO.
	Na própria DASHBOARD deve haver uma tabela que mostre os eventos, pondo ao topo os últimos adicionados ou alterados, mostrando seus NOME, DESCRIÇÃO, DATA, HORÁRIO E LOCAL. LISTA servirá apenas para fins de agrupamento na pesquisa, então não serão exibidos.
	O botão EDITAR EVENTO, deve permitir que o usuário acesse uma página com todos os EVENTOs. Serão listados pelo nome, e ao lado de cada um, haverá dois botões, EDITAR e APAGAR. EDITAR redirecionará o usuário para uma tela que trará o evento selecionado com os respectivos campos preenchidos, ou vazios, de acordo com o que foi previamente feito pelo próprio criador dele. O usuário poderá editar o evento e então selecionar a opção SALVAR MODIFICAÇÃO, a qual modificará os dados na tabela no mesmo ID que o EVENTO originalmente era. Depois ele redirecionará automaticamente para a tela de opções de edição. Para facilitar as buscas, deve haver um campo para a entrada do nome de um EVENTO, e ele deve ser localizado e exibido, para que o usuário possa editá-lo ou apaga-lo. No fim, deve também haver uma opção de voltar à DASHBOARD.
	Como funcionalidade adicional, haverá um botão para exibir todos os EVENTOs e procurá-los por ORDEM ALFABÉTICA, RECENTEMENTE CRIADOS/MODIFICADOS, POR MARCADOR/LISTA. Assim facilitando a separação deles e sua identificação. 
	O programa não deve: 
- Mostrar dados de EVENTO que não seja do USUÁRIO atual da sessão.
- Fechar o programa, exceto ao fechar a janela pelo DASHBOARD ou pela TELA INICIAL. 
- Mostrar eventos que já foram apagados, nem criar eventos em duplicata ou que não foram realmente salvos.

Ferramentas utilizadas:
- Computadores com sistema Windows 10 e/ou 11.
- Apache MAVEN, para estruturação e aplicação em Java.
- Java 24, linguagem empregada.
- NetBeans (Para prototipagem dos Frames/Páginas)
- VSCode, para codagem, edição e compilação do código.
- MySql, como plataforma de banco de dados.
- Copilot, para assistência durante a codagem e testes do programa.
- ChatGPT e Google, para pesquisa e estruturação de comandos SQL.
