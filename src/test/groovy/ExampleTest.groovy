import au.com.dius.pact.consumer.groovy.messaging.PactMessageBuilder
import au.com.dius.pact.core.model.messaging.Message
import groovy.json.JsonSlurper
import spock.lang.Specification

import static au.com.dius.pact.consumer.groovy.Matchers.string
import static java.nio.charset.StandardCharsets.UTF_8

class ExampleTest extends Specification {

    def 'example'() {
        given:
        def pactMessageBuilder = new PactMessageBuilder().with {
            serviceConsumer 'consumer'
            hasPactWith 'provider'
            expectsToReceive 'feed entry'
            withMetaData(contentType: 'application/json')
            withContent {
                type 'foo'
                data {
                    reference {
                        id string('abc')
                    }
                }
            }
        }

        expect:
        pactMessageBuilder.run { Message message ->
            def feedEntry = new String(message.contentsAsBytes(), UTF_8)
            assert new JsonSlurper().parseText(feedEntry) == [
                    type: 'foo',
                    data: [
                            reference: [
                                    id: 'abc'
                            ]
                    ]
            ]
        }
    }
}
