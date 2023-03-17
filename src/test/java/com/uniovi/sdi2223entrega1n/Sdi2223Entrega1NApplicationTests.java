package com.uniovi.sdi2223entrega1n;

import com.uniovi.sdi2223entrega1n.entities.User;
import com.uniovi.sdi2223entrega1n.pageobjects.*;
import com.uniovi.sdi2223entrega1n.repositories.OffersRepository;
import com.uniovi.sdi2223entrega1n.services.UsersService;
import com.uniovi.sdi2223entrega1n.util.SeleniumUtils;
import org.junit.jupiter.api.*;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class Sdi2223Entrega1NApplicationTests {

    static String PathFirefox = "C:\\Program Files\\Mozilla Firefox\\firefox.exe";
    // TODO: Eliminar y dejar una ruta
    //static String Geckodriver  ="A:\\Escritorio\\PL-SDI-Sesión5-material\\PL-SDI-Sesión5-material\\geckodriver-v0.30.0-win64.exe";
    //static String Geckodriver = "C:\\Path\\geckodriver-v0.30.0-win64.exe";
    //static String Geckodriver = "C:\\Users\\Tomás\\Downloads\\OneDrive_1_7-3-2023\\PL-SDI-Sesión5-material\\geckodriver-v0.30.0-win64.exe";
    //static String Geckodriver = "C:\\Users\\UO253628\\Downloads\\PL-SDI-Sesión5-material\\PL-SDI-Sesión5-material\\geckodriver-v0.30.0-win64.exe";
    static String Geckodriver = "C:\\Users\\kikoc\\Dev\\sellenium\\geckodriver-v0.30.0-win64.exe";
    //static String PathFirefox = "/Applications/Firefox.app/Contents/MacOS/firefox-bin";
    //Ruta Manu (cambiar)
    //static String Geckodriver = "C:\\Users\\Usuario\\Desktop\\SDI\\sesion5\\PL-SDI-Sesión5-material\\geckodriver-v0.30.0-win64.exe";
    static WebDriver driver = getDriver(PathFirefox, Geckodriver);
    static String URL = "http://localhost:8090";

    // Endpoint para mostrar el listado de usuarios (Ver UserController)
    static final String USER_LIST_ENDPOINT = URL + "/user/list";

    // Endpoint para mostrar la vista de conversaciones de un usuario.
    static final String CONVERSATION_LIST_ENDPOINT = URL + "/conversation/list";

    // Endpoint para mostrar la vista de dashboard (logs)
    static final String ADMIN_DASHBOARD_ENDPOINT = URL + "/admin/logs";


    // Enpoint para mostrar la vista de login
    static final String LOGIN_ENDPOINT = URL + "/login";

    @Autowired
    private OffersRepository offersRepository;

    @Autowired
    private UsersService userService;

    public static WebDriver getDriver(String PathFirefox, String Geckodriver) {
        System.setProperty("webdriver.firefox.bin", PathFirefox);
        System.setProperty("webdriver.gecko.driver", Geckodriver);
        driver = new FirefoxDriver();
        return driver;
    }

    @BeforeEach
    public void setUp() {
        driver.navigate().to(URL);
    }

    //Después de cada prueba se borran las cookies del navegador
    @AfterEach
    public void tearDown() {
        driver.manage().deleteAllCookies();
    }

    //Antes de la primera prueba
    @BeforeAll
    static public void begin() {
    }

    //Al finalizar la última prueba
    @AfterAll
    static public void end() {
        // Cerramos el navegador al finalizar las pruebas
        driver.quit();
    }

    //    [Prueba1] Registro de Usuario con datos válidos.
    @Test
    @Order(1)
    void PR01() {
        //Nos movemos al formulario de registro
        PO_HomeView.clickOption(driver, "signup", "class", "btn btn-primary");
        //Cumplimentamos el registro con datos VALIDOS
        PO_SignUpView.fillForm(driver, "JoseFo@gmail.com", "Josefo", "Perez", "77777", "77777");
        //Comprobamos que hemos ido a la pagina de home, confirmando que el registro se ha completado con exito
        PO_HomeView.checkWelcomeToPage(driver, PO_Properties.getSPANISH());
    }

    //    [Prueba2] Registro de Usuario con datos inválidos (email vacío, nombre vacío, apellidos vacíos).
    @Test
    @Order(2)
    void PR02() {
        //Nos movemos al formulario de registro
        PO_HomeView.clickOption(driver, "signup", "class", "btn btn-primary");
        //Cumplimentamos el registro con datos INVALIDOS
        PO_SignUpView.fillForm(driver, "", "", "", "77777", "77777");
        //Comprobamos que seguimos en la pantalla de registro
        PO_SignUpView.checkSignUpPage(driver, PO_Properties.getSPANISH());
    }

    //    [Prueba3] Registro de Usuario con datos inválidos (repetición de contraseña inválida).
    @Test
    @Order(3)
    void PR03() {
        //Nos movemos al formulario de registro
        PO_HomeView.clickOption(driver, "signup", "class", "btn btn-primary");
        //Cumplimentamos el registro con datos INVALIDOS
        PO_SignUpView.fillForm(driver, "JoseFo@gmail.com", "Josefo", "Perez", "77777", "773777");
        //Comprobamos que seguimos en la pantalla de registro
        PO_SignUpView.checkSignUpPage(driver, PO_Properties.getSPANISH());
    }

    //    [Prueba4] Registro de Usuario con datos inválidos (email existente).
    @Test
    @Order(4)
    void PR04() {
        //Nos movemos al formulario de registro
        PO_HomeView.clickOption(driver, "signup", "class", "btn btn-primary");
        //Cumplimentamos el registro con datos VALIDOS
        PO_SignUpView.fillForm(driver, "JoseFo1@gmail.com", "Josefo", "Perez", "77777", "77777");
        //Comprobamos que seguimos en la pantalla de registro
        PO_HomeView.checkWelcomeToPage(driver, PO_Properties.getSPANISH());

        //Nos movemos al formulario de registro
        PO_HomeView.clickOption(driver, "logout", "class", "btn btn-primary");
        PO_HomeView.clickOption(driver, "signup", "class", "btn btn-primary");
        //Cumplimentamos el registro con datos INVALIDOS
        PO_SignUpView.fillForm(driver, "JoseFo1@gmail.com", "Josefo", "Perez", "77777", "77777");
        //Comprobamos que seguimos en la pantalla de registro
        PO_SignUpView.checkSignUpPage(driver, PO_Properties.getSPANISH());
    }

    //[Prueba5] Inicio de sesión con datos válidos (administrador).
    @Test
    @Order(5)
    void PR05() {
        //Nos movemos al formulario de inicio de sesión
        PO_HomeView.clickOption(driver, "login", "class", "btn btn-primary");
        //Rellenamos con datos validos del usuario administrador
        PO_LoginView.fillForm(driver, "admin@email.com", "admin");
        //Comprobamos que hemos ido a la pagina de home, confirmando que el inicio de sesión se ha completado con exito
        PO_HomeView.checkWelcomeToPage(driver, PO_Properties.getSPANISH());
    }

    //[Prueba6] Inicio de sesión con datos válidos (usuario estándar).
    @Test
    @Order(6)
    void PR06() {
        //Nos movemos al formulario de inicio de sesión
        PO_HomeView.clickOption(driver, "login", "class", "btn btn-primary");
        //Rellenamos con datos validos del usuario estandar
        PO_LoginView.fillForm(driver, "usuario1@email.com", "123456");
        //Comprobamos que hemos ido a la pagina de home, confirmando que el inicio de sesión se ha completado con exito
        PO_HomeView.checkWelcomeToPage(driver, PO_Properties.getSPANISH());
    }

    //[Prueba7] Inicio de sesión con datos inválidos (usuario estándar, campo email y contraseña vacíos)
    @Test
    @Order(7)
    void PR07() {
        //Nos movemos al formulario de inicio de sesión
        PO_HomeView.clickOption(driver, "login", "class", "btn btn-primary");
        //Rellenamos con datos INVALIDOS
        PO_LoginView.fillForm(driver, "", "");
        //Comprobamos que seguimos en la pantalla de inicio de sesión
        PO_LoginView.checkLoginPage(driver, PO_Properties.getSPANISH());
    }

    //[Prueba11] Mostrar el listado de usuarios y comprobar que se muestran todos los que existen en el
    //sistema.
    @Test
    @Order(11)
    void PR011() {
        //Nos movemos al formulario de inicio de sesion
        PO_HomeView.clickOption(driver, "login", "class", "btn btn-primary");
        //Cumplimentamos el registro con datos VALIDOS
        PO_LoginView.fillForm(driver, "admin@email.com", "admin");
        //Comprobamos que seguimos en la pantalla de registro
        PO_HomeView.checkWelcomeToPage(driver, PO_Properties.getSPANISH());
        //Accedemos a la lista de users
        driver.get("http://localhost:8090/user/list");


        //Sacamos la lista de usuarios q hay
        List<WebElement> usersList = PO_UserListView.getUsersList(driver);
        //Sacamos la lista de usaurios del sistema
        List<User> usersSystem = userService.getUsers();
        Assertions.assertEquals(usersSystem.size(), usersSystem.size());
        //Comprobamos uno a uno
        PO_UserListView.compareOneByOneTwoUsersLists(driver, usersList, usersSystem);
    }

    //[Prueba12] Ir a la lista de usuarios, borrar el primer usuario de la lista, comprobar que la lista se actualiza
    //y dicho usuario desaparece.
    @Test
    @Order(12)
    void PR012() {
        //Nos movemos al formulario de inicio de sesion
        PO_HomeView.clickOption(driver, "login", "class", "btn btn-primary");
        //Cumplimentamos el registro con datos VALIDOS
        PO_LoginView.fillForm(driver, "admin@email.com", "admin");
        //Comprobamos que seguimos en la pantalla de registro
        PO_HomeView.checkWelcomeToPage(driver, PO_Properties.getSPANISH());
        //Accedemos a la lista de users
        driver.get("http://localhost:8090/user/list");

        //Sacamos la lista de usuarios q hay
        List<WebElement> usersList = PO_UserListView.getUsersList(driver);
        //guardamos tamaño para comporbar
        int s1 = usersList.size();
        //Primer usuario y marcaje de su checkbox
        WebElement firstUser = usersList.get(0);

        PO_UserListView.markCheckBoxUser(driver, firstUser);
        //Borramos dandole al boton
        PO_UserListView.clickDeleteButton(driver);
        //Actualizamos la lista
        usersList = PO_UserListView.getUsersList(driver);
        //Guardamos segundo tamaño y vemos q no es el mismo, comprobamos que decrementó en 1
        int s2 = usersList.size();
        Assertions.assertNotEquals(s1, s2);
        Assertions.assertEquals(s1, s2 + 1);
    }

    //[Prueba13] Ir a la lista de usuarios, borrar el último usuario de la lista, comprobar que la lista se actualiza
//y dicho usuario desaparece.
    @Test
    @Order(13)
    void PR013() {
        //Nos movemos al formulario de inicio de sesion
        PO_HomeView.clickOption(driver, "login", "class", "btn btn-primary");
        //Cumplimentamos el registro con datos VALIDOS
        PO_LoginView.fillForm(driver, "admin@email.com", "admin");
        //Comprobamos que seguimos en la pantalla de registro
        PO_HomeView.checkWelcomeToPage(driver, PO_Properties.getSPANISH());
        //Accedemos a la lista de users
        driver.get("http://localhost:8090/user/list");

        //Sacamos la lista de usuarios q hay
        List<WebElement> usersList = PO_UserListView.getUsersList(driver);
        //guardamos tamaño para comporbar
        int s1 = usersList.size();
        //Ultimo usuario y marcaje de su checkbox
        WebElement lastUser = usersList.get(usersList.size() - 1);
        PO_UserListView.markCheckBoxUser(driver, lastUser);
        //Borramos dandole al boton
        PO_UserListView.clickDeleteButton(driver);
        //Actualizamos la lista
        usersList = PO_UserListView.getUsersList(driver);
        //Guardamos segundo tamaño y vemos q no es el mismo, comprobamos que decrementó en 1
        int s2 = usersList.size();
        Assertions.assertNotEquals(s1, s2);
        Assertions.assertEquals(s1, s2 + 1);
    }

    //[Prueba14] Ir a la lista de usuarios, borrar 3 usuarios, comprobar que la lista se actualiza y dichos
    //usuarios desaparecen.
    @Test
    @Order(14)
    void PR014() {
        //Nos movemos al formulario de inicio de sesion
        PO_HomeView.clickOption(driver, "login", "class", "btn btn-primary");
        //Cumplimentamos el registro con datos VALIDOS
        PO_LoginView.fillForm(driver, "admin@email.com", "admin");
        //Comprobamos que seguimos en la pantalla de registro
        PO_HomeView.checkWelcomeToPage(driver, PO_Properties.getSPANISH());
        //Accedemos a la lista de users
        driver.get("http://localhost:8090/user/list");

        //Sacamos la lista de usuarios q hay
        List<WebElement> usersList = PO_UserListView.getUsersList(driver);
        //guardamos tamaño para comporbar
        int s1 = usersList.size();
        //Sacamos los tres primeros usuarios y marcamos de sus checkboxes
        WebElement u1 = usersList.get(0);
        WebElement u2 = usersList.get(1);
        WebElement u3 = usersList.get(2);
        PO_UserListView.markCheckBoxUser(driver, u1);
        PO_UserListView.markCheckBoxUser(driver, u2);
        PO_UserListView.markCheckBoxUser(driver, u3);
        //Borramos dandole al boton
        PO_UserListView.clickDeleteButton(driver);
        //Actualizamos la lista
        usersList = PO_UserListView.getUsersList(driver);
        //Guardamos segundo tamaño y vemos q no es el mismo, comprobamos que decrementó en 1
        int s2 = usersList.size();
        Assertions.assertNotEquals(s1, s2);
        Assertions.assertEquals(s1, s2 + 3);
    }

    // [Prueba 15]. Añadir nueva oferta con datos válidos.
    @Test
    @Order(15)
    public void PR015() {
        // Crear nuevo usuario
        SeleniumUtils.registerNewUser(driver, "miemail444@email.com", "123456");

        String newOfferText = "Coche marca Renault 1";

        // Acceder a la vista de añadir una nueva oferta
        PO_NavView.selectDropdownById(driver, "gestionOfertasMenu", "gestionOfertasDropdown", "addOfferMenu");

        // Rellenar campos del formulario con valores válidos.
        PO_OfferView.fillForm(driver, newOfferText, "Coche de los años 90", 2000.50);

        // Comprobar que se muestra en el listado de ofertas
        List<WebElement> offers = PO_View.checkElementBy(driver, "text", newOfferText);
        Assertions.assertEquals(1, offers.size());
    }

    // [Prueba 16]. Añadir una nueva oferta con datos inválidos -> precio negativo.
    @Test
    @Order(16)
    public void PR016() {
        // Iniciar sesion
        SeleniumUtils.signInIntoAccount(driver, "STANDARD", "miemail444@email.com");

        // Acceder a la vista de añadir una nueva oferta
        PO_NavView.selectDropdownById(driver, "gestionOfertasMenu", "gestionOfertasDropdown", "addOfferMenu");

        // Rellenar campos del formulario con valores inválidos.
        PO_OfferView.fillForm(driver, "Coche marca Renault", "Coche de los años 90", -1.0);

        // Comprobar que se muestra el error en el formulario.
        PO_OfferView.checkErrorMessage(driver, PO_Properties.getSPANISH(), "error.offer.price.range");
    }

    // [Prueba 17]. Listado de ofertas propias de un usuario. Comprobar que se muestran todas las ofertas
    // publicadas por dicho usuario.
    @Test
    @Order(17)
    public void PR017() {
        // Iniciar sesion
        SeleniumUtils.signInIntoAccount(driver, "STANDARD", "miemail444@email.com");

        // Acceder a la vista de listado de ofertas
        PO_NavView.selectDropdownById(driver, "gestionOfertasMenu", "gestionOfertasDropdown", "listOfferMenu");

        // Comprobar número elementos de tabla con número de elementos BBDD
        int offerCountFromUserOnDatabase = offersRepository.findAllBySeller("miemail444@email.com").size();

        // Obtener número de filas de la tabla de la vista del listado de ofertas
        int rowCount = SeleniumUtils.countTableRows(driver, "//table[@class='table table-hover']/tbody/tr");

        // Verificar que el número de registros mostrados es correcto
        Assertions.assertEquals(offerCountFromUserOnDatabase, rowCount);
    }

    // [Prueba18]. Baja de una oferta - Borrar primera oferta de la lista.
    @Test
    @Order(18)
    public void PR018() {
        // Registrar nuevo usuario
        SeleniumUtils.registerNewUser(driver, "miemail666@email.com", "123456");

        String sampleText1 = "Coche de segunda mano";
        String sampleText2 = "Vestido de comunión";
        String sampleText3 = "Baraja de cartas";

        // Iniciar sesion, añadir una nueva oferta
        PO_OfferView.addSampleOffer(driver, sampleText1);
        PO_OfferView.addSampleOffer(driver, sampleText2);
        PO_OfferView.addSampleOffer(driver, sampleText3);

        // Click en el enlace eliminar del primer elemento de la lista
        PO_OfferView.clickDeleteButton(driver, 0);

        // Comprobar que el primer elemento no se muestra en la tabla
        PO_OfferView.checkOfferNotExistsOnPage(driver, sampleText1);

        // Comprobar que el número de oferta es 3
        List<WebElement> offerList = SeleniumUtils.waitLoadElementsBy(driver, "free", "//tbody/tr",
                PO_View.getTimeout());
        Assertions.assertEquals(2, offerList.size());
    }

    // [Prueba19]. Baja de una oferta - Borrar última oferta de la lista.
    @Test
    @Order(19)
    public void PR019() {
        // Registrar nuevo usuario
        SeleniumUtils.registerNewUser(driver, "miemail777@email.com", "123456");

        String sampleTextLast = "Coche se segunda mano";
        String sampleText2 = "Vestido de comunión";
        String sampleText3 = "Baraja de cartas";

        // Iniciar sesion, añadir una nueva oferta
        PO_OfferView.addSampleOffer(driver, sampleText2);
        PO_OfferView.addSampleOffer(driver, sampleText3);
        PO_OfferView.addSampleOffer(driver, sampleTextLast);

        // Click en el enlace eliminar del ultimo elemento de la lista
        PO_OfferView.clickDeleteButton(driver, 2);

        // Comprobar que el ultimo elemento no se muestra en la tabla
        PO_OfferView.checkOfferNotExistsOnPage(driver, sampleTextLast);

        // Comprobar que el número de ofertas, tras eliminar la última oferta, es 2
        List<WebElement> offerList = SeleniumUtils.waitLoadElementsBy(driver, "free", "//tbody/tr",
                PO_View.getTimeout());
        Assertions.assertEquals(2, offerList.size());

    }

    /**
     * [Prueba 20] Hacer una búsqueda con el campo vacío y comprobar que se muestra la página que
     * corresponde con el listado de las ofertas existentes en el sistema
     */
    @Test
    @Order(20)
    public void PR020(){
        PO_HomeView.clickOption(driver, "login", "class", "btn btn-primary");
        //Rellenamos con datos validos del usuario estandar
        PO_LoginView.fillForm(driver, "usuario7@email.com", "123456");
        String invalid = "";
        PO_AllOfferView.SearchInvalid(driver,invalid);

        //Comprobar que el numero es 0
        List<WebElement> offerList = SeleniumUtils.waitLoadElementsBy(driver, "free", "//tbody/tr",
                PO_View.getTimeout());

        Assertions.assertEquals(5,offerList.size());

        //Cierro sesion
        PO_HomeView.clickOption(driver, "logout", "class", "btn btn-primary");
    }

    /**
     * [Prueba21] Hacer una búsqueda escribiendo en el campo un texto que no exista y comprobar que se
     * muestra la página que corresponde, con la lista de ofertas vacía.
     */
    @Test
    @Order(21)
    public void PR021(){
        PO_HomeView.clickOption(driver, "login", "class", "btn btn-primary");
        //Rellenamos con datos validos del usuario estandar
        PO_LoginView.fillForm(driver, "usuario7@email.com", "123456");
        String invalid = "asdasd";
        PO_AllOfferView.SearchInvalid(driver, invalid);

        //Comprobar que el numero es 0
        SeleniumUtils.textIsNotPresentOnPage(driver, "//tbody/tr");

        //Cierro sesion
        PO_HomeView.clickOption(driver, "logout", "class", "btn btn-primary");
    }

    // [Prueba 30]. Acceso sin autenticación a la opción de listado de usuarios.
    @Test
    @Order(30)
    public void PR30() {
        // Acceso por url sin estar autenticado.
        driver.navigate().to(USER_LIST_ENDPOINT);

        // Comprobar que se redirecciona a la vista de login
        Assertions.assertEquals(LOGIN_ENDPOINT, driver.getCurrentUrl());
    }

    // [Prueba 31]. Acceso sin autenticación al listado de conversaciones de un
    // usuario estandar. Se debe redireccionar al formulario de login.
    @Test
    @Order(31)
    public void PR31() {
        // Acceso por url sin estar autenticado.
        driver.navigate().to(CONVERSATION_LIST_ENDPOINT);

        // Comprobar que se redirecciona a la vista de login
        Assertions.assertEquals(LOGIN_ENDPOINT, driver.getCurrentUrl());
    }

    // [Prueba 32]. Usuario estandar autenticado accede a una opción disponible solo
    // para usuarios administradores.
    @Test
    @Order(32)
    public void PR32() {
        String email = "miemail888@email.com";

        // Registrar nuevo usuario
        SeleniumUtils.registerNewUser(driver, email, "123456");

        // Acceder a una opción para usuarios administradores.
        // Escenario 1: Ver logs
        driver.navigate().to(ADMIN_DASHBOARD_ENDPOINT);

        // Comprobar que se redirecciona a la vista de login
        Assertions.assertEquals(LOGIN_ENDPOINT, driver.getCurrentUrl());

    }

    // [Prueba 33]. Usuario administrador autenticado visualiza los logs generados
    // en una serie de iteraciones. Generar al menos dos interacciones de cada
    // tipo y comprobar que el listado incluye los correspondientes logs.
    @Test
    @Order(33)
    public void PR33() {

        // -- TIPO LOGIN-EX
        // Iniciar sesion como admin - LOG-EX
        SeleniumUtils.signInIntoAccount(driver, "ADMIN", "admin@email.com");

        // Cerrar la sesión del usuario admin
        PO_NavView.clickOption(driver, "logout", "class", "btn btn-primary");

        String userEmail1 = "user0000234@email.com";

        // Registrar nuevo usuario
        // -- TIPO ALTA
        SeleniumUtils.registerNewUser(driver, userEmail1, "123456");

        // Iniciar sesión con el usuario creado e introducir la contraseña mal - LOGIN-ERR
        // -- TIPO LOGIN-ERR
        SeleniumUtils.signInIntoAccount(driver, "STANDARD", userEmail1, "123");

        // -- TIPO LOGIN-EX
        SeleniumUtils.signInIntoAccount(driver, "STANDARD", userEmail1, "123456");

        // -- TIPO PET
        PO_NavView.selectDropdownById(driver, "", "", "");
        PO_NavView.selectDropdownById(driver, "", "", "");

        // -- TIPO LOGOUT
        // Cerrar la sesión del usuario 1
        PO_NavView.clickOption(driver, "logout", "class", "btn btn-primary");

        String userEmail2 = "user0000444@email.com";

        // Registrar nuevo usuario
        // -- TIPO ALTA
        SeleniumUtils.registerNewUser(driver, userEmail2, "123456");

        // Iniciar sesión con el usuario creado e introducir la contraseña mal - LOGIN-ERR
        SeleniumUtils.signInIntoAccount(driver, "STANDARD", userEmail2, "123");

        // -- TIPO PET
        PO_NavView.selectDropdownById(driver, "", "", "");
        PO_NavView.selectDropdownById(driver, "", "", "");

        // -- TIPO LOGOUT
        // Cerrar la sesión del usuario 2
        PO_NavView.clickOption(driver, "logout", "class", "btn btn-primary");

        // Total de logs esperado:
        int expectedLogs = 13;

        // Volver a iniciar sesion como admin
        SeleniumUtils.signInIntoAccount(driver, "ADMIN", "admin@email.com");

        // Ir a la vista de logs
        PO_NavView.clickOption(driver, "/admin/logs", "id", "viewLogsMenuItem");

        // Obtener el número de logs de la tabla
        int rowCount = SeleniumUtils.countTableRows(driver, "//table[@class='table']/tbody/tr");

        // Comprobar que el número de registros mostrados es correcto
        Assertions.assertEquals(expectedLogs, rowCount);
    }

    // [Prueba 34]. Autenticado como usuario administrador, ir a visualización de
    // logs, pulsar en el botón de borrar logs y comprobar que se eliminan los logs
    // de la base de datos.
    @Test
    @Order(34)
    public void PR34() {
        // Iniciar sesión como administrador
        SeleniumUtils.signInIntoAccount(driver, "ADMIN", "admin@email.com");

        // Ir a la vista de logs
        PO_NavView.clickOption(driver, "/admin/logs", "id", "viewLogsMenuItem");

        // Hacer click en el boton de eliminar logs
        PO_NavView.clickOption(driver, "/admin/logs/deleteAll", "id", "deleteAllLogsButton");

        int rowCount = SeleniumUtils.countTableRows(driver, "//table[@class='table']/tbody/tr");

        // Comprobar que el número de registros mostrados es correcto
        // Comprobar que se muestra la peticion de eliminar logs realizada, es decir,
        // un único registro.
        Assertions.assertEquals(1, rowCount);

    }

}
