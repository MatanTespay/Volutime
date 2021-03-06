package network.utils;

/**
 * NetworkResListener interface
 */
public interface NetworkResListener {
    /**
     * callback method which called when the resources update is started
     */
    public void onPreUpdate(String type);

    /**
     * callback method which called after resources update is finished
     * @param  res  - the data
     * @param status - the status of the update process
     */
    public void onPostUpdate(byte[] res, ResStatus status, String type);
}
