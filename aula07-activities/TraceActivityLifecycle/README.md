# TraceActivityLifecycle

Demonstra o ciclo de vida de Atividades. A aplicação, no estilo "Hello World", inclui duas Atividades e um Intent. Na primeira Atividade, que inclui uma caixa de texto e um botão, o usuário digita seu nome. Na segunda Atividade, o nome do usuário (transferido por meio de um Intent) é impresso em uma mensagem de saudação. 

Ambas as atividades estão instrumentadas para reportar cada estágio de seus ciclos de vida. Isso foi realizado por meio da sobrecarga de todos os métodos de *callback* de cada atividade, e incluindo-se em cada um desses métodos uma mensagem que identifica o estado correspondente do ciclo de vida. 

As transições do ciclo de vida podem ser observadas no LOGCAT do aplicação. Para simular, basta interagir com a aplicação via seu caso de uso normal. Recomenda-se também o uso do botão *back* e forçar uma mudança de configuração (por exemplo, mudança de orientação da tela), pois essas ações também afetam o ciclo de vida das Atividades.
