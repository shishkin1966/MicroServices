**Пример реализации Service Locator в Clean Architecture на языке Kotlin**

Архитектура реализует следующие цепочки событий:

источники(view <-> презентер, прочие объекты) -> запрос -> исполнитель (провайдер DB/NET/пр.) -> сообщение с результатом -> мессенджер <-> получатели сообщений(презентеры, прочие объекты)

объекты(запросы) -> изменение Observable объектов(Broadcast, Content Provider, прочих) -> оповещение подписчиков Observable объектов
 
Т.к. начиная с провайдера и заканчивая месенджером объекты не зависят от жизненного цикла View, то на этом участке возможно
использование потоковой обработки событий(реактивного программирования)

Жизненный цикл презентора совпадает с циклом жизни View, к которой он связан. Взаимодействие 
между ними осуществляется через единственный метод интерфеса IActionHandler:

- *addAction(action: iAction)* - добавить Action 

В основе архитектуры лежит модель общественного разделения труда и парадигма функционального программирования. Все объекты делятся на генераторы сервиса и пользователей(потребителей) сервисов. Выделены следующие единицы:
- провайдер(интерфейс IProvider) - одиночный объект, который предоставляет сервис любым пользователям(объектам) без их учета
- объекты-подписчики (интерфейс IProviderSubscriber), которые регистрируются в объединениях для предоставления общего сервиса
- малые объединения подписчиков(интерфейс ISmallUnion) - объект, объединяющий группу подписчиков(IProviderSubscriber) для предоставления общего сервиса
- объединения подписчиков(интерфейс IUnion) - объект, объединяющий группу подписчиков(IProviderSubscriber) для предоставления расширенной функциональности

Регистрацию подписчиков и их объединений, а также провайдеров и их взаимное связывание, отмену регистрации осуществляет сервис локатор (IServiceLocator).
Все объекты учитываемые сервис локатором реализуют интерфейс ISubscriber, включающий в себя интерфейсы IValidated и INamed. Интерфейс INamed, содержит метод:

- *getName(): String* - получить имя объекта.

Интерфейс IValidated определяет следующий метод:

- *isValid(): Boolean* - проверить состояние объекта

провайдер(IProvider) - объект, предоставляющий какую-либо функциональность другим объектам.  
Состав интерфейса IProvider (наследника INamed и IValidated):
- *isPersistent()*: Boolean – флаг, определяющий тип провайдера - постоянный/выгружаемый - т.е определяет разрешение операции отмены регистрации
- *onUnRegister()* – событие, которое предваряет операцию отмену регистрации 
	провайдера. Используется для внутренних операций в провайдере, необходимых перед отменой 
	регистрации
- *onRegister()* – событие, которое завершает операцию регистрации провайдера
- *stop()* - остановить работу провайдера

провайдеры делятся на постоянные(isPersistent), выгружаемые и короткоживущие. Короткоживущий провайдеры
самостоятельно выгружаются из памяти при отсутствии активности потребителей своего сервиса.

Объединение(ISmallUnion или IUnion) подписчиков (классов, реализующие интерфейс IProviderSubscriber) учитывает подписчиков только одного типа. Интерфейс IProviderSubscriber, является наследником интерфейсов ISubscriber и имеет методы:
- *getProviderSubscription(): List<String>* – получить список подписки с именами объединений (список имен объединений, в которые он будет входить)
- *onStopProvider(Provider: IProvider)* - событие - остановлена работа провайдера

Интерфейс ISmallUnion является наследником интерфейса Provider и имеет методы:
- *createSecretary(): ISecretary<T>* - получить секретаря объединения(объект учитывающий подписчиков)
- *register(subscriber: T): Boolean* – зарегистрировать подписчика
- *unregister(subscriber: T)* – отписать подписчика
- *unregister(name: String)* - отписать подписчика по его имени
- *getSubscribers(): List<T>* - получить список всех подписчиков
- *getValidatedSubscribers(): List<T>* - получить список валидных подписчиков
- *getReadySubscribers(): List<T>* - получить список готовых подписчиков. Т.е. подписчиков, которые поддерживают состояния(IStateable) и находятся в соостянии отличном от CREATE и DESTROY
- *hasSubscribers(): Boolean* - проверить наличие подписчиков
- *hasSubscriber(name: String): Boolean* - проверить наличие подписчика
- *getSubscriber(name: String): T?)* – получить подписчика по его имени.
- *onRegisterFirstSubscriber()* - событие - появился первый подписчик
- *onUnRegisterLastSubscriber()* - событие - отписан последний подписчик
- *onAddSubscriber(subscriber: T)* - событие - добавлен подписчик
- *contains(subscriber: T): Boolean* - проверить наличие подписчика

Наследником интерфейса ISmallUnion является IUnion. Состав методов:
- *getCurrentSubscriber(): T?* - получить текущего подписчика объединения 
- *setCurrentSubscriber(subscriber: T): Boolean* – установить текущего подписчика у объединения

Как было указано, управлением провайдеров и объединений занимается администратор, 
реализующий интерфейс IServiceLocator:
- *exists(name: String): Boolean* - Проверить существование провайдера с указанным именем
- *<C : IProvider> get(name: String): C?* – получить провайдера/объединение по его имени
- *registerProvider(Provider: IProvider): Boolean* - зарегистрировать провайдера/объединение. Используется только 
	для Singleton объектов
- *registerProvider(name: String): Boolean* – зарегистрировать провайдера/объединение, предварительно создав 
	его. Предпочительный метод.
- *unregisterProvider(name: String): Boolean* - отменить регистрацию провайдера/объединения
- *registerSubscriber(subscriber: IProviderSubscriber): Boolean* – зарегистрировать подписчика объединений
- *unregisterSubscriber(subscriber: IProviderSubscriber): Boolean* – отменить регистрацию подписчика
- *setCurrentSubscriber(subscriber: IProviderSubscriber): Boolean* – сделать подписчика текущим. Администратор сам 
	сделает текущим данного подписчика у всех объединений, на которые подписан объект
- *start()* - старт Service Locator
- *stop()* - остановка Service Locator
- *getProviders(): List<IProvider>* - получить список провайдеров
- *getProviderFactory(): IProviderFactory* - получить фабрику провайдеров

В составе данной реализации описаны следующие провайдеры/объединения:
- *ErrorProvider(Singleton)* - провайдер для регистрации ошибок. Предоставляет функционал регистрации ошибок в ходе работы приложения
- *CrashProvider* - провайдер, получающий управление при незапланированных прерываниях приложения
- *MessengerUnion* - объединение для учета почтовых клиентов или мессенджер. Обеспечивает хранение и гарантированную доставку сообщений. Сообщения хранятся в объединении и подписчики читают их при переходе в состояние STATE_READY и при подключению к месседжеру. Если получатель сообщения находится в состоянии STATE_READY, то сообщения доставляются сразу же. Возможен контроль сообщений на наличие дубликатов, при этом будут удалятся все предыдущие копии данного сообщения. Поддерживаются списки рассылки. Объединение хранит почтовые сообщения независимо от цикла жизни подписчиков(почта подписчика сохраняется при уничтожении подписчика и доступна при его повторном создании). Возможно добавление сообщений только активным получателям.
- *PresenterUnion* - объединение, регистрирующее презентеры и обеспечивающий доступ к ним. Презентеры могут быть локальными,   т.е. регистрируются только локально или глобальные, ссылку на которые можно получить через данное объединение. 
- *ObservableUnion* - объединение, отвечающее за учет Observable объектов. Поддерживаются Content Provider Observable объекты, Broadcast Observable и слушатели именованных объектов(например таблиц БД). Каждый Observable объект может иметь нескольких слушателей. Старт подписки на событие проводиться при появлении первого слушателя Observable объекта, а отписка - при отмене регистрации последнего слушателя. 
- *LocationUnion* - объединение, предоставляющее сервис геолокации
- *NetProvider* - реализация провайдера для выборки данных в формате JSON из сети
- *NetCbProvider* - реализация провайдера для выборки данных в формате XML из сети
- *DbProvider* - реализация провайдера для выборки данных из БД SQLite. Допускается соединение сразу с несколькими БД.
- *NotificationProvider* - короткоживущий провайдер, предоставляющий сервис вывода сообщений в зону уведомлений
- *CommonExecutor* - провайдер для выполнения запросов приложения
- *DbExecutor* - провайдер для выполнения запросов к БД
- *NetExecutor* - провайдер для выполнения запросов по интернету
