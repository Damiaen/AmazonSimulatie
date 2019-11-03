package com.nhlstenden.amazonsimulatie.tests;

import com.nhlstenden.amazonsimulatie.models.Model;
import com.nhlstenden.amazonsimulatie.models.Object3D;
import com.nhlstenden.amazonsimulatie.views.DefaultWebSocketView;

import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class DefaultWebSocketViewTests {

    /**
     * De code hieronder is een voorbeeld van een test in Java. Je schrijf per class die je wilt testen
     * een testclass zoals deze. Daarin zet je al je testfuncties. Vaak schrijf je per methode (of functie)
     * die je wilt testen een methode zoals deze hieronder. Kijk in de methode naar de genummerde comments.
     * Om de test het beste te begrijpen, begin je vanaf comment #1.
     */
	@Test
	public void testUpdateSignal() throws Exception {
        /**
         * Comment #2
         * Deze code hieronder hebben we nodig om de DefaultWebSocketView van comment 1 te kunnen maken.
         * Dat onderdeel van de software heeft een zogenaamde WebSocketSession nodig (een onderdeel waarmee
         * informatie via een websocket naar de webbrowser kan worden verstuurd). Een WebSocketSession is
         * normaal gesproken een onderdeel van het websocket systeem van onze server, en we kunnen niet
         * zomaar een WebSocketSession aanmaken wanneer er geen server draait, laat staan geen browser is.
         * Om dit toch te kunnen gebruiken, 'mocken' we de class WebSocketSession. Dit mocken betekent dat
         * we als het ware een 'nep' versie van het object maken. Deze 'mockSession'n (zie hieronder) is
         * een object dat wel alle methoden heeft van een echte WebSocketSession, maar de methoden doen
         * simpelweg niks. Hierdoor kun je code die een WebSocketSession nodig heeft, toch laten werken.
         * Ga nu naar comment #3.
         */
        WebSocketSession mockSession = mock(WebSocketSession.class);

        /**
         * Comment #3
         * De code hieronder is eigenlijk de invulling van een methode van WebSocketSession. Zoals je in
         * comment #2 las, is de het object mockSession een 'lege' WebSocketSession. De methoden bestaan dus
         * wel, maar doen in feite niks. Hieronder wordt een invulling gegeven. De when() functie is onderdeel
         * van het systeem om te mocken, en zegt hier wanneer de methode .isOpen() wordt aangeroepen op het object
         * mockSession, dan moet er iets gebeuren. Wat er moet gebeuren staat achter de when() methode, in de
         * vorm van .thenReturn(true). De hele regel code betekent nu dat wanneer .isOpen() wordt aangeroepen
         * op mockSession, dan moet deze methode altijd de waarde true teruggeven. Dit onderdeel is nodig omdat
         * de view in de update methode gebruikmaakt van de .isOpen() methode. Ga nu naar comment #4.
         */
        when(mockSession.isOpen()).thenReturn(true);

        /**
         * Comment 4
         * De code hieronder is misschien wat onduidelijk. Wat we hier doen, is het injecteren van testcode binnen
         * het mockSession object. Dit doen we omdat mockSession gebruikt wordt om het JSON pakketje te versturen.
         * Dit object krijgt dus sowieso de JSON tekst te zien. Dit gebeurd in de methode .sendMessage(). Hier wordt
         * een tekstbericht verstuurd naar de browser. In dit bericht zit de JSON code. Hier kunnen we dus een test
         * injecteren om de validiteit van de JSON de controleren. Dit is ook wat hieronder gebeurd. De methode
         * doAnwser hieronder zorgt ervoor dat de code die daarin wordt gegeven wordt uitgevoerd wanneer de
         * methode .sendMessage wordt aangeroepen. Het onderdeel daarna met 'new TextMessage(anyString())'
         * betekent dat die nieuwe code moet worden uitgevoerd voor de methode .sendMessage(), wanneer er
         * aan .sendMessage() een object TextMessage wordt meegegeven waar elke bedenkbare string in mag
         * zitten (vandaar de anyString()). Ga nu naar comment #5.
         */
        doAnswer(new Answer<Void>() {
            public Void answer(InvocationOnMock invocation) {
            Object[] args = invocation.getArguments();

            /**
             * Comment #5
             * Deze code hieronder is de daadwerkelijke testcode. De methode assertThat() controleert of iets dat je opgeeft
             * dat waar moet zijn, daadwerkelijk ook zo is. In dit geval wordt gecontroleerd of het binnengekomen object
             * TextMessage (zit op index 0 van args) een JSON string in zich heeft die gelijk is aan de opgegeven string
             * daar rechts van. Via de is() methode wordt aangegeven dat beide strings gelijk moeten zijn. De JSON wordt
             * gemaakt van een Object3D dat geupdate wordt (zie update methode in de DefaultWebSocketView class). In de
             * JSON code hieronder zie je dat voor elke parameter in de JSON een standaardwaarde is ingevoerd. Ga nu naar
             * comment #6 om te zien hoe we ervoor zorgen dat de update() methode ook gebruiktmaakt van die standaardwaarden.
             */
            assertThat(((TextMessage)args[0]).getPayload(), is("{\"command\": \"object_update\",\"parameters\": {\"uuid\":\"unique_string\",\"type\":\"type_here\",\"x\":0.0,\"y\":0.0,\"z\":0.0,\"rotationX\":0.0,\"rotationY\":0.0,\"rotationZ\":0.0,\"status\":\"status_here\"}}"));
            
            return null;
            }
        }).when(mockSession).sendMessage(new TextMessage(anyString()));

        /**
         * Comment #1
         * De testfunctie waar we nu inzitten heet testUpdateSignal. Dit updatesignal slaat op het updaten van een
         * view binnen de simulatie. Wanneer dat gebeurd, wordt er een JSON pakketje naar de webbrowser van die
         * view gestuurd. We gaan dit systeem simuleren om zo de validiteit van de JSON te kunnen testen. Daarvoor
         * hebben we eerst een nieuwe view nodig. Die wordt hieronder aangemaakt. Om een DefaultWebSocketView aan te
         * maken, hebben we iets van een websocket nodig (in dit geval een sessie, zie de projectcode). Zie comment #2.
         */
        DefaultWebSocketView view = new DefaultWebSocketView(mockSession);

        /**
         * Commeent #6
         * Hieronder wordt een Object3D aangemaakt. Dit doen we omdat de view.update() methode een Object3D nodig heeft.
         * Voor een Object3D geldt ook dat een simulatie nodig is om een Object3D object te kunnen krijgen. Omdat we de
         * simulatie niet draaien, mocken we een Object3D. We maken er dus als het ware eentje na. Hier voeren we de
         * standaardwaarden in die de JSON code bij comment #5 gebruikt om te controleren of de .update() methode van
         * view werkt. Ga nu naar comment #7 om te zien welke code de test in zijn werk zet.
         */
        Object3D mockObj3D = mock(Object3D.class);
        when(mockObj3D.getUUID()).thenReturn("unique_string");
        when(mockObj3D.getType()).thenReturn("type_here");
        when(mockObj3D.getX()).thenReturn(0.0);
        when(mockObj3D.getY()).thenReturn(0.0);
        when(mockObj3D.getZ()).thenReturn(0.0);
        when(mockObj3D.getRotationX()).thenReturn(0.0);
        when(mockObj3D.getRotationY()).thenReturn(0.0);
        when(mockObj3D.getRotationZ()).thenReturn(0.0);
        when(mockObj3D.getStatus()).thenReturn("status_here");

        /**
         * Comment #7
         * De code hieronder activeert de .update() methode van view. Deze methode maakt van een Object3D (hier mockObj3D)
         * een JSON pakket en verstuurd deze via een websocket connectie. In de websocket connectie hebben we onze testcode
         * geïnjecteerd, en dit betekent dat dan de test ook zal worden uitgoerd.
         */
        view.update(Model.UPDATE_COMMAND, mockObj3D);
	}
}