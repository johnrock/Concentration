package android.os;

/**
 * @author John Piser johnpiser@yahoo.com
 *
 * Shadow class of AsyncTask to allow for testing components that use it.
 */

public abstract class AsyncTask<Params, Progress, Result> {

    abstract Result doInBackground(Params...params);

    void onPostExecute(Result result){

    }

    void onProgressUpdate(Progress...values){

    }

    public AsyncTask<Params, Progress, Result> execute(Params...params){
        return this;
    }
}
