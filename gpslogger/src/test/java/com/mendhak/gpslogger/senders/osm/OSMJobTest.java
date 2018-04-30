package com.mendhak.gpslogger.senders.osm;

import android.test.suitebuilder.annotation.SmallTest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.File;

import oauth.signpost.OAuthConsumer;
import oauth.signpost.exception.OAuthCommunicationException;
import oauth.signpost.exception.OAuthExpectationFailedException;
import oauth.signpost.exception.OAuthMessageSignerException;
import oauth.signpost.http.HttpParameters;
import oauth.signpost.http.HttpRequest;
import oauth.signpost.signature.OAuthMessageSigner;
import oauth.signpost.signature.SigningStrategy;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

@SmallTest
@RunWith(MockitoJUnitRunner.class)
public class OSMJobTest {
    OAuthConsumer consumer = new OAuthConsumer() {
        @Override
        public void setMessageSigner(OAuthMessageSigner messageSigner) {}

        @Override
        public void setAdditionalParameters(HttpParameters additionalParameters) {}

        @Override
        public void setSigningStrategy(SigningStrategy signingStrategy) {}

        @Override
        public void setSendEmptyTokens(boolean enable) {}

        @Override
        public HttpRequest sign(HttpRequest request) throws OAuthMessageSignerException, OAuthExpectationFailedException, OAuthCommunicationException {
            return null;
        }

        @Override
        public HttpRequest sign(Object request) throws OAuthMessageSignerException, OAuthExpectationFailedException, OAuthCommunicationException {
            return null;
        }

        @Override
        public String sign(String url) throws OAuthMessageSignerException, OAuthExpectationFailedException, OAuthCommunicationException {
            return null;
        }

        @Override
        public void setTokenWithSecret(String token, String tokenSecret) {

        }

        @Override
        public String getToken() {
            return null;
        }

        @Override
        public String getTokenSecret() {
            return null;
        }

        @Override
        public String getConsumerKey() {
            return null;
        }

        @Override
        public String getConsumerSecret() {
            return null;
        }

        @Override
        public HttpParameters getRequestParameters() {
            return null;
        }
    };

    @Test
    public void shouldReRunOnThrowable_Test() {
        OSMJob job = new OSMJob(consumer, "url", new File("file"),
                "description", "tag", "visibility");
        assertThat(job.shouldReRunOnThrowable(new Throwable()), is(false));
    }

    @Test
    public void getJobTag_Test(){
        OSMJob job = new OSMJob(consumer, "url", new File("file"),
                "description", "tag", "visibility");
        assertThat(job.getJobTag(new File("file")), is("OSMfile"));
    }
}