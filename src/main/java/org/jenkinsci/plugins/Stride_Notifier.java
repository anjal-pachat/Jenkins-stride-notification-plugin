package org.jenkinsci.plugins;


import hudson.EnvVars;
import hudson.Extension;
import hudson.Launcher;
import hudson.model.BuildListener;
import hudson.model.AbstractBuild;
import hudson.model.AbstractProject;
import hudson.tasks.BuildStepDescriptor;
import hudson.tasks.BuildStepMonitor;
import hudson.tasks.Notifier;
import hudson.tasks.Publisher;
import java.io.DataOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import net.sf.json.JSONObject;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.StaplerRequest;

public class Stride_Notifier extends Notifier {

 private final String access_token;
 private final String url;
 @DataBoundConstructor
 public Stride_Notifier(final String access_token,final String url) 
 {
  this.access_token =access_token;
  this.url = url;
 }
 public String getAccess_token() 
  {
   return access_token;
  }
 public String getUrl() {
   return url;
  }
 @Override
 public boolean perform(
 @SuppressWarnings("rawtypes") final AbstractBuild build,
 final Launcher launcher, final BuildListener listener) {
 
  try {
  	 EnvVars envVars = build.getEnvironment(listener); 
	 String job = envVars.get("JOB_NAME");
	 listener.getLogger().println(envVars.get("BUILD_URL"));
     String message = " JOB_NAME : **"+job+"# "+
    		  			envVars.get("BUILD_NUMBER")+"** "+
    		  			" STATUS : **_"+ build.getResult()+"_**"+
    		  			" Duration: **_"+build.getTimestampString()+"_**"+
    		  			" URL: "+envVars.get("BUILD_URL");
     listener.getLogger().println("Sending build details to stride group..");
     boolean b =sendStrideMessage(listener,message);
     if(b==true){
	  listener.getLogger().println("Build result send to stride group..");
     }else{
      listener.getLogger().println("Error occured while sending message to stride");
     }
	  
  	} catch (Exception e) {
  		  	listener.getLogger().printf("Error Occurred .....: %s ", e);
  	}
 
  return true;
 }

 @Override
 public DescriptorImpl getDescriptor() 
 {
  return (DescriptorImpl) super.getDescriptor();
 }

 @Extension
 public static class DescriptorImpl extends BuildStepDescriptor<Publisher>  
 {
  public DescriptorImpl() {
  load();
 }

 @Override
 public boolean configure(StaplerRequest req, JSONObject formData)
 throws FormException {
  save();
  return super.configure(req, formData);
 }
 @Override
 public boolean isApplicable(
 @SuppressWarnings("rawtypes") Class<? extends AbstractProject> jobType) {
  // Indicates that this builder can be used with all kinds of project 
  // types.
  return true;
 }

 @Override
 public String getDisplayName() {
  return "Stride Notification";
 }
 }

 public BuildStepMonitor getRequiredMonitorService() {
 return BuildStepMonitor.NONE;
 }

 private boolean sendStrideMessage(BuildListener listener,String msg) throws Exception {
   
   String url_orig = url;
   String token =access_token;
   String basicAuth ="Bearer "+token;
   URL url = new URL(url_orig);
    HttpURLConnection conn = (HttpURLConnection) url.openConnection(); 
	conn.setDoOutput(true);
	conn.setRequestMethod("POST");
	conn.setRequestProperty("Content-Type", "text/markdown");
	conn.setRequestProperty ("Authorization", basicAuth);
	byte[] postData = msg.getBytes("utf-8");
	DataOutputStream wr = new DataOutputStream(conn.getOutputStream());
	wr.write(postData);
	  if (conn.getResponseCode() != 201) 
	  {
		   throw new RuntimeException("Failed : HTTP error code : "+ conn.getResponseCode());
	   } else{
		 wr.close();
		 conn.disconnect();
	  }
    return true;
  }
}