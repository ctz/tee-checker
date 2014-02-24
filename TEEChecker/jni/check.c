#include <stdio.h>
#include <string.h>

#include "com_ifihada_teechecker_MobiCore.h"
#include "MobiCoreDriverApi.h"

static const char * mcResult2str(mcResult_t res)
{
  switch (res)
  {
    case MC_DRV_NO_NOTIFICATION:  return "No notification available";
    case MC_DRV_ERR_NOTIFICATION:  return "Error during notification on communication level";
    case MC_DRV_ERR_NOT_IMPLEMENTED:  return "Function not implemented";
    case MC_DRV_ERR_OUT_OF_RESOURCES:  return "No more resources available";
    case MC_DRV_ERR_INIT:  return "Driver initialization failed";
    case MC_DRV_ERR_UNKNOWN:  return "Unknown error";
    case MC_DRV_ERR_UNKNOWN_DEVICE:  return "The specified device is unknown";
    case MC_DRV_ERR_UNKNOWN_SESSION:  return "The specified session is unknown";
    case MC_DRV_ERR_INVALID_OPERATION:  return "The specified operation is not allowed";
    case MC_DRV_ERR_INVALID_RESPONSE:  return "The response header from the MC is invalid";
    case MC_DRV_ERR_TIMEOUT:  return "Function call timed out";
    case MC_DRV_ERR_NO_FREE_MEMORY:  return "Can not allocate additional memory";
    case MC_DRV_ERR_FREE_MEMORY_FAILED:  return "Free memory failed";
    case MC_DRV_ERR_SESSION_PENDING:  return "Still some open sessions pending";
    case MC_DRV_ERR_DAEMON_UNREACHABLE:  return "MC daemon not reachable";
    case MC_DRV_ERR_INVALID_DEVICE_FILE:  return "The device file of the kernel module could not be opened";
    case MC_DRV_ERR_INVALID_PARAMETER:  return "Invalid parameter";
    case MC_DRV_ERR_KERNEL_MODULE:  return "Error from Kernel Module, see DETAIL for errno";
    case MC_DRV_ERR_BULK_MAPPING:  return "Error during mapping of additional bulk memory to session";
    case MC_DRV_ERR_BULK_UNMAPPING:  return "Error during unmapping of additional bulk memory to session";
    case MC_DRV_INFO_NOTIFICATION:  return "Notification received, exit code available";
    case MC_DRV_ERR_NQ_FAILED:  return "Set up of NWd connection failed";
    case MC_DRV_ERR_DAEMON_VERSION:  return "Wrong daemon version";
    case MC_DRV_ERR_CONTAINER_VERSION:  return "Wrong container version";
    case MC_DRV_ERR_WRONG_PUBLIC_KEY:  return "System Trustlet public key is wrong";
    case MC_DRV_ERR_CONTAINER_TYPE_MISMATCH:  return "Wrong containter type(s)";
    case MC_DRV_ERR_CONTAINER_LOCKED:  return "Container is locked (or not activated)";
    case MC_DRV_ERR_SP_NO_CHILD:  return "SPID is not registered with root container";
    case MC_DRV_ERR_TL_NO_CHILD:  return "UUID is not registered with sp container";
    case MC_DRV_ERR_UNWRAP_ROOT_FAILED:  return "Unwrapping of root container failed";
    case MC_DRV_ERR_UNWRAP_SP_FAILED:  return "Unwrapping of service provider container failed";
    case MC_DRV_ERR_UNWRAP_TRUSTLET_FAILED:  return "Unwrapping of Trustlet container failed";
    default:
      return "unknown";
  }
}


JNIEXPORT jstring JNICALL Java_com_ifihada_teechecker_MobiCore_native_1getVersion
   (JNIEnv *env, jclass kl)
{
#define MAX_BUF_SZ 2048
  char buf[MAX_BUF_SZ] = { 0 };

  mcResult_t rc;
  mcVersionInfo_t verf;
  uint32_t dev = MC_DEVICE_ID_DEFAULT;

  memset(&verf, 0, sizeof verf);

  rc = mcOpenDevice(dev);
  if (rc)
  {
    snprintf(buf, sizeof buf, "(mcOpenDevice(0x%08x) failed: %s [0x%08x])", dev, mcResult2str(rc), rc);
    goto x_ret;
  }
  
  rc = mcGetMobiCoreVersion(dev, &verf);
  mcCloseDevice(dev);
  if (rc)
  {
    snprintf(buf, sizeof buf, "(mcGetMobiCoreVersion(0x%08x) failed: %s [0x%08x])", dev, mcResult2str(rc), rc);
    goto x_ret;
  }

  snprintf(buf, sizeof buf,
           "Product-ID: %s\n"
           "Control interface version: 0x%08x\n"
           "Secure object version: 0x%08x\n"
           "Load format version: 0x%08x\n"
           "Container format version: 0x%08x\n"
           "Configuration block format version: 0x%08x\n"
           "Trustlet API version: 0x%08x\n"
           "Driver API version: 0x%08x\n"
           "Content management protocol version: 0x%08x\n",
           verf.productId,
           verf.versionMci,
           verf.versionSo,
           verf.versionMclf,
           verf.versionContainer,
           verf.versionMcConfig,
           verf.versionTlApi,
           verf.versionDrApi,
           verf.versionCmp);
  
x_ret:
  return (*env)->NewStringUTF(env, buf);
}
