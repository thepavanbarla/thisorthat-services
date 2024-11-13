package com.tot.utils;

import java.io.FileInputStream;
import java.io.IOException;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import com.google.firebase.auth.UserRecord;

/**
 * @author karthik on 18/01/22.
 * @project totservices
 */

public class FirebaseSDK {

    @SuppressWarnings("deprecation")
	public static void main(String[] args) throws IOException, FirebaseAuthException {
        FileInputStream serviceAccount = new FileInputStream(
            "/Users/karthik/Downloads/this-or-that-50b96-firebase-adminsdk-fkeu4-4639cbb04f.json");

        FirebaseOptions options = new FirebaseOptions.Builder()
            .setCredentials(GoogleCredentials.fromStream(serviceAccount)).build();

        FirebaseApp.initializeApp(options);

        UserRecord userRecord = FirebaseAuth.getInstance().getUser("XXQ0UuX3vw1AZhWOr4NP3PPtyJbHF2XX");
        // See the UserRecord reference doc for the contents of userRecord.
        System.out.println("Successfully fetched user data: " + userRecord.getUid());
        System.out.println("Successfully fetched user data: " + userRecord.getPhoneNumber());
        System.out.println("Successfully fetched user data: " + userRecord.getDisplayName());


        String customToken = FirebaseAuth.getInstance().createCustomToken("Q0UuX3vw1AZhWOr4NP3PPtyJbHF2");
        System.out.println(customToken);


        //verify token id
        //use result.user.getIdToken() to send requests to server

        //TODO:- check refresh tokens, votes, analytics, notification, comments with like count and userids, comments sort oldest first
        //add update phone number api

        //vote, comment, follow, follow_request, accepted

        String idToken = "eyJhbGciOiJSUzI1NiIsImtpZCI6IjQwMTU0NmJkMWRhMzA0ZDc2NGNmZWUzYTJhZTVjZDBlNGY2ZjgyN2IiLCJ0eXAiOiJKV1QifQ.eyJpc3MiOiJodHRwczovL3NlY3VyZXRva2VuLmdvb2dsZS5jb20vdGhpcy1vci10aGF0LTUwYjk2IiwiYXVkIjoidGhpcy1vci10aGF0LTUwYjk2IiwiYXV0aF90aW1lIjoxNjQyNTM1NjYwLCJ1c2VyX2lkIjoiUTBVdVgzdncxQVpoV09yNE5QM1BQdHlKYkhGMiIsInN1YiI6IlEwVXVYM3Z3MUFaaFdPcjROUDNQUHR5SmJIRjIiLCJpYXQiOjE2NDI1MzU2NjAsImV4cCI6MTY0MjUzOTI2MCwicGhvbmVfbnVtYmVyIjoiKzkxOTc1Mzk4Njk4OCIsImZpcmViYXNlIjp7ImlkZW50aXRpZXMiOnsicGhvbmUiOlsiKzkxOTc1Mzk4Njk4OCJdfSwic2lnbl9pbl9wcm92aWRlciI6InBob25lIn19.W3eyiZshCGG71xYaQQU5NxXx-23SMQ2FXj8D55XfUdo5isKPccJOBgd7b5q-QrWMv8RWt_4K9NznWPNARiEuHrUBApp4yo4pJFoWOsWaq9v_GIBKZv7bkDDZE_9UFfZd0Y-MkPTowSu_y6EhbINQBTaed0sG6m19j9VZQsrHsRjh65UtlLTSVw-P8-MInGkDsrMcTceUO-zgOOKJqd_YbGjkzxsgP6ZTZvUKV4pM71yZBY7zRRcl39D2lpNt5qTN-csdH5vMNYwNKmyzQTjkMrti1KycbmnW6nhZkZNnIQQBzcx_lPyJ9xece6Gkh0aJbPMu17HF5XH2QO7U_3fxfQ";
        FirebaseToken decodedToken =
            FirebaseAuth.getInstance().verifyIdToken(idToken);
        System.out.println(decodedToken.getUid());

    }

}
