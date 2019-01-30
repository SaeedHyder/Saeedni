package com.ingic.saeedni.helpers;

import android.accounts.Account;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Log;


import com.google.android.gms.auth.GoogleAuthException;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.plus.Plus;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.people.v1.People;
import com.google.api.services.people.v1.model.Birthday;
import com.google.api.services.people.v1.model.Gender;
import com.google.api.services.people.v1.model.Person;
import com.ingic.saeedni.R;
import com.ingic.saeedni.fragments.abstracts.BaseFragment;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;


public class GoogleHelper implements GoogleApiClient.OnConnectionFailedListener {

    private static final String TAG = GoogleHelper.class.getSimpleName();
    private static final int RC_SIGN_IN = 7;
    public static GoogleHelper mGoogleHelper;
    private GoogleApiClient mGoogleApiClient;
    private GoogleHelperInterfce googleHelperInterface;
    private BaseFragment context;
    private GoogleSignInOptions gso;
    private boolean isGoogleSign;
    private ProgressDialog progressDialog;
    private GoogleLoginEnt googleLoginEnt = new GoogleLoginEnt();

    public static GoogleHelper getInstance() {

        if (mGoogleHelper == null) {
            mGoogleHelper = new GoogleHelper();
        }
        return mGoogleHelper;
    }

    public void DisconnectGoogleApi() {

        mGoogleApiClient.stopAutoManage(context.getActivity());
        mGoogleApiClient.disconnect();
    }

    public void ConnectGoogleAPi() {
        mGoogleApiClient.connect();
    }

    public void configGoogleApiClient(BaseFragment activity, GoogleHelperInterfce interfce) {
        this.context = activity;
        this.googleHelperInterface = interfce;
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestProfile()
                .build();

        this.mGoogleApiClient = new GoogleApiClient.Builder(context.getContext())
                .enableAutoManage(context.getActivity(), this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
    }

    public void configGoogleApiClient(BaseFragment activity) {
        this.context = activity;
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestProfile()
                .build();

        this.mGoogleApiClient = new GoogleApiClient.Builder(context.getContext())
                .enableAutoManage(context.getActivity(), this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .addApi(Plus.API)
                .build();
    }

    public void setGoogleHelperInterface(GoogleHelperInterfce interfce) {
        this.googleHelperInterface = interfce;

    }

    public Scope[] setScopes() {
        return this.gso.getScopeArray();
    }

    public void intentGoogleSign() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        context.getActivity().startActivityForResult(signInIntent, RC_SIGN_IN);
        showProgressDialog();
    }

    public void googleSignOut() {
        // showProgressDialog();
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        hideProgressDialog();
                        setGoogleSign(false);
                    }
                });
    }

    public void googleRevokeAccess() {
        Auth.GoogleSignInApi.revokeAccess(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        hideProgressDialog();
                        setGoogleSign(false);
                    }
                });
    }

    public boolean isGoogleSign() {
        return isGoogleSign;
    }

    private void setGoogleSign(boolean isSignin) {
        this.isGoogleSign = isSignin;
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        // An unresolvable error has occurred and Google APIs (including Sign-In) will not
        // be available.
        hideProgressDialog();
        Log.d(TAG, "onConnectionFailed:" + connectionResult);
    }

    private void showProgressDialog() {
        progressDialog = ProgressDialog.show(context.getContext(), "", context.getString(R.string.please_wait), true);
    }

    private void hideProgressDialog() {
        if (this.progressDialog != null)
            this.progressDialog.dismiss();
    }

    public void checkGoogleSeesion() {
        OptionalPendingResult<GoogleSignInResult> opr = Auth.GoogleSignInApi.silentSignIn(mGoogleApiClient);
        if (opr.isDone()) {
            // If the user's cached credentials are valid, the OptionalPendingResult will be "done"
            // and the GoogleSignInResult will be available instantly.
            Log.d(TAG, "Got cached sign-in");
            GoogleSignInResult result = opr.get();
            setGoogleSign(true);
            setGoogleResultData(result.getSignInAccount());

        } else {
            // If the user has not previously signed in on this device or the sign-in has expired,
            // this asynchronous branch will attempt to sign in the user silently.  Cross-device
            // single sign-on will occur in this branch.
            showProgressDialog();
            opr.setResultCallback(new ResultCallback<GoogleSignInResult>() {
                @Override
                public void onResult(GoogleSignInResult googleSignInResult) {
                    setGoogleSign(true);
                    setGoogleResultData(googleSignInResult.getSignInAccount());
                }
            });
        }
    }

    public void handleGoogleResult(int requestCode, int resultCode, Intent data) {
        hideProgressDialog();
        if (requestCode == RC_SIGN_IN) {
            hideProgressDialog();
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            Log.d(TAG, "handleGoogleSignInResult:" + result.isSuccess());
            if (result.isSuccess()) {
                // Signed in successfully, show authenticated UI.
                GoogleSignInAccount acct = result.getSignInAccount();
                hideProgressDialog();
                setGoogleSign(true);
                setGoogleResultData(acct);


            } else {
                hideProgressDialog();
                setGoogleSign(false);
            }
        }
    }

    private void setGoogleResultData(GoogleSignInAccount acct) {
        if (acct != null) {
            googleLoginEnt.setGoogleUID(acct.getId());
//            googleLoginEnt.setGoogleToken(acct.getIdToken());
            googleLoginEnt.setGoogleFullName(acct.getDisplayName());
            googleLoginEnt.setGoogleFirstName(acct.getGivenName());
            googleLoginEnt.setGoogleLastName(acct.getFamilyName());
            googleLoginEnt.setGoogleEmail(acct.getEmail());
            googleLoginEnt.setGoogleUProfilePicture(acct.getPhotoUrl() == null ? "" : acct.getPhotoUrl().toString());
            new getProfileInfoFromEmail().execute(googleLoginEnt.getGoogleEmail());
        }
    }

    public interface GoogleHelperInterfce {
        void onGoogleSignInResult(GoogleLoginEnt result);
    }

    private class getProfileInfoFromEmail extends AsyncTask<String, Void, Void> {
        HttpTransport httpTransport = new NetHttpTransport();
        JacksonFactory jsonFactory = new JacksonFactory();
        Calendar calendar = Calendar.getInstance();

        // Retrieved from the sigin result of an authorized GoogleSignIn
        String personEmail;

        @Override
        protected Void doInBackground(String... params) {
            personEmail = params[0];
            Person userProfile = null;
            Collection<String> scopes = new ArrayList<>(Collections.singletonList(Scopes.PROFILE));
            if (context != null) {


                GoogleAccountCredential credential =
                        GoogleAccountCredential.usingOAuth2(context.getContext(), scopes);
                credential.setSelectedAccount(new Account(personEmail, "com.google"));

                People service = new People.Builder(httpTransport, jsonFactory, credential)
                        .setApplicationName(context.getContext().getString(R.string.app_name))// your app name
                        .build();
                final String SCOPES = "https://www.googleapis.com/auth/plus.login ";
                String token = null;
                // Get info. on user
                try {
                    token = credential.getToken();/* GoogleAuthUtil.getToken(
                        context.getContext(),
                        Plus.AccountApi.getAccountName(mGoogleApiClient),
                        "oauth2:" + SCOPES);*/
                    userProfile = service.people().get("people/me").setRequestMaskIncludeField("person.addresses,person.age_ranges,person.biographies,person.birthdays,person.bragging_rights,person.cover_photos,person.email_addresses,person.events,person.genders,person.im_clients,person.interests,person.locales,person.memberships,person.metadata,person.names,person.nicknames,person.occupations,person.organizations,person.phone_numbers,person.photos,person.relations,person.relationship_interests,person.relationship_statuses,person.residences,person.skills,person.taglines,person.urls").execute();
                } catch (IOException e) {
                    Log.e(TAG, e.getMessage(), e);
                } catch (GoogleAuthException e) {
                    e.printStackTrace();
                }

                if (token != null) {
                    googleLoginEnt.setGoogleToken(token);
                }
            }
            // Get whatever you want
            if (userProfile != null) {
                List<Gender> genders = userProfile.getGenders();
                String gender = null;
                if (genders != null && genders.size() > 0) {
                    gender = genders.get(0).getValue();
                    googleLoginEnt.setGoogleGender(genders.get(0).getValue());

                } else {
                    googleLoginEnt.setGoogleGender("default");
                }
                List<Birthday> birthdays = userProfile.getBirthdays();
                if (birthdays != null && birthdays.size() > 0) {
                    com.google.api.services.people.v1.model.Date bday = birthdays.get(0).getDate();
                    calendar.set(bday.getYear(), bday.getMonth(), bday.getDay());
                    googleLoginEnt.setGoogleBirthday("");
//                    googleLoginEnt.setGoogleBirthday(DateHelper.getFormattedDate(calendar.getTime(), "yyyy-MM-dd"));

                } else {
                    googleLoginEnt.setGoogleBirthday("");
                }

            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            googleHelperInterface.onGoogleSignInResult(googleLoginEnt);
        }
    }

    public class GoogleLoginEnt {
        private String GoogleUID;
        private String GoogleUProfilePicture;
        private String GoogleToken;
        private String GoogleFirstName;
        private String GoogleLastName;
        private String GoogleEmail;
        private String GoogleGender;
        private String GoogleBirthday;
        private String GoogleLocation;
        private String GoogleLink;
        private String GoogleFullName;

        public String getGoogleUID() {
            return GoogleUID;
        }

        public void setGoogleUID(String googleUID) {
            GoogleUID = googleUID;
        }

        public String getGoogleUProfilePicture() {
            return GoogleUProfilePicture;
        }

        public void setGoogleUProfilePicture(String googleUProfilePicture) {
            GoogleUProfilePicture = googleUProfilePicture;
        }

        public String getGoogleToken() {
            return GoogleToken;
        }

        public void setGoogleToken(String googleToken) {
            GoogleToken = googleToken;
        }

        public String getGoogleFirstName() {
            return GoogleFirstName;
        }

        public void setGoogleFirstName(String googleFirstName) {
            GoogleFirstName = googleFirstName;
        }

        public String getGoogleLastName() {
            return GoogleLastName;
        }

        public void setGoogleLastName(String googleLastName) {
            GoogleLastName = googleLastName;
        }

        public String getGoogleEmail() {
            return GoogleEmail;
        }

        public void setGoogleEmail(String googleEmail) {
            GoogleEmail = googleEmail;
        }

        public String getGoogleGender() {
            return GoogleGender==null?"":GoogleGender;
        }

        public void setGoogleGender(String googleGender) {
            GoogleGender = googleGender;
        }

        public String getGoogleBirthday() {
            return GoogleBirthday == null ? "" : GoogleBirthday;
        }

        public void setGoogleBirthday(String googleBirthday) {
            GoogleBirthday = googleBirthday;
        }

        public String getGoogleLocation() {
            return GoogleLocation;
        }

        public void setGoogleLocation(String googleLocation) {
            GoogleLocation = googleLocation;
        }

        public String getGoogleLink() {
            return GoogleLink;
        }

        public void setGoogleLink(String googleLink) {
            GoogleLink = googleLink;
        }

        public String getGoogleFullName() {
            return GoogleFullName;
        }

        public void setGoogleFullName(String googleFullName) {
            GoogleFullName = googleFullName;
        }


    }
}


