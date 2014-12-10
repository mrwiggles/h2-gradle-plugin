package org.jamescarr.gradle
import org.gradle.tooling.BuildLauncher
import org.gradle.tooling.GradleConnector
import org.gradle.tooling.ProjectConnection
import org.junit.After
import org.junit.BeforeClass
import org.junit.Test
import org.testng.annotations.AfterClass

class StartH2TaskTest {
    static BuildLauncher launcher
    static ProjectConnection connection
    private static final ByteArrayOutputStream stream = new ByteArrayOutputStream()

    @BeforeClass
    static void beforeAll() {
        GradleConnector connector = GradleConnector.newConnector()
        installLocally(connector)
        connector.forProjectDirectory(new File("src/test/resources/basic-project"))
        connection = connector.connect()
        launcher = connection.newBuild()
        launcher.setStandardOutput(stream)
    }

    @AfterClass
    static void after() {
        connection.close()
    }

    @After
    void stop() {
        launcher.forTasks("h2stop")
        try {
            launcher.run()
        } catch (Exception e) {
            // we don't care if this fails, it means the server might not even have been started
        }

    }

    static void installLocally(GradleConnector conn) {
        conn.forProjectDirectory(new File("."))
        ProjectConnection connection = conn.connect()
        BuildLauncher launcher = connection.newBuild()
        launcher.forTasks("install")
        launcher.run()
    }

    @Test
    public void "should have h2start and h2stop tasks"() {
        launcher.forTasks("tasks")
        launcher.run()

        assert stream.toString().contains('H2 tasks')
        assert stream.toString().contains('h2start - Starts an embedded h2 database.')
        assert stream.toString().contains('h2stop - Stops an embedded h2 database.')
    }

    @Test
    void "h2start task should start an h2 database tcp server"() {
        launcher.forTasks("h2start")
        launcher.run()

        assert stream.toString().contains("Web Console server running at http://localhost:8089 (only local connections)")
        assert stream.toString().contains("TCP server running at tcp://localhost:9011 (only local connections)")
    }
}
