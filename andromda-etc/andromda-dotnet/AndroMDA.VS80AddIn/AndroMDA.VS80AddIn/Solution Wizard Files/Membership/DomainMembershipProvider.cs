//
// DomainMembershipProvider
//

#region Using statements

using System;
using System.Web;
using System.Web.Hosting;
using System.Web.Security;
using System.Web.Configuration;
using System.Security.Principal;
using System.Security.Permissions;
using System.Globalization;
using System.Runtime.Serialization;
using System.Collections;
using System.Collections.Specialized;
using System.Security.Cryptography;
using System.Text;
using System.Text.RegularExpressions;
using System.Configuration.Provider;
using System.Configuration;
using System.Web.DataAccess;
using System.Web.Management;
using System.Web.Util;

#endregion

using ${wizard.solution.name}.Domain;
using ${wizard.solution.name}.VO;
using ${wizard.solution.name}.Service;

namespace ${wizard.projects.web.common.name}
{

	public class DomainMembershipProvider : MembershipProvider
	{

		private const string PASSWORD_SALT = "DomainMembershipProviderSalt";
		private const int DEFAULT_AUTOGENERATED_PASSWORD_SIZE = 14;

		#region Member Variables

		private bool _EnablePasswordRetrieval;
		private bool _EnablePasswordReset;
		private bool _RequiresQuestionAndAnswer;
		private string _AppName;
		private bool _RequiresUniqueEmail;
		private string _HashAlgorithmType;
		private int _MaxInvalidPasswordAttempts;
		private int _PasswordAttemptWindow;
		private int _MinRequiredPasswordLength;
		private int _MinRequiredNonalphanumericCharacters;
		private string _PasswordStrengthRegularExpression;
		private MembershipPasswordFormat _PasswordFormat;

        private IMembershipService _membershipService = null;

		#endregion

		#region Properties

		////////////////////////////////////////////////////////////
		// Public properties

		public override bool EnablePasswordRetrieval
		{
			get { return _EnablePasswordRetrieval; }
		}

		public override bool EnablePasswordReset
		{
			get { return _EnablePasswordReset; }
		}

		public override bool RequiresQuestionAndAnswer
		{
			get { return _RequiresQuestionAndAnswer; }
		}

		public override bool RequiresUniqueEmail
		{
			get { return _RequiresUniqueEmail; }
		}

		public override MembershipPasswordFormat PasswordFormat
		{
			get { return _PasswordFormat; }
		}

		public override int MaxInvalidPasswordAttempts
		{
			get { return _MaxInvalidPasswordAttempts; }
		}

		public override int PasswordAttemptWindow
		{
			get { return _PasswordAttemptWindow; }
		}

		public override int MinRequiredPasswordLength
		{
			get { return _MinRequiredPasswordLength; }
		}

		public override int MinRequiredNonAlphanumericCharacters
		{
			get { return _MinRequiredNonalphanumericCharacters; }
		}

		public override string PasswordStrengthRegularExpression
		{
			get { return _PasswordStrengthRegularExpression; }
		}

		public override string ApplicationName
		{
			get { return _AppName; }
			set
			{
				if (_AppName != value)
				{
					_AppName = value;
				}
			}
		}

        private IMembershipService MembershipService
        {
            get
            {
                if (_membershipService == null)
                {
                    _membershipService = new MembershipService();
                }
                return _membershipService;
            }
        }

		#endregion

		#region Initialization

		public override void Initialize(string name, NameValueCollection config)
		{
			if (config == null)
			{
				throw new ArgumentNullException("config");
			}

			if (String.IsNullOrEmpty(name))
			{
				name = "DomainMembershipProvider";
			}

			if (string.IsNullOrEmpty(config["description"]))
			{
				config.Remove("description");
				config.Add("description", "Membership $safeprojectname$ Provider");
			}

			base.Initialize(name, config);

			_EnablePasswordRetrieval = GetBooleanValue(config, "enablePasswordRetrieval", false);
			_EnablePasswordReset = GetBooleanValue(config, "enablePasswordReset", false);
			_RequiresQuestionAndAnswer = GetBooleanValue(config, "requiresQuestionAndAnswer", false);
			_RequiresUniqueEmail = GetBooleanValue(config, "requiresUniqueEmail", true);
			_MaxInvalidPasswordAttempts = GetIntValue(config, "maxInvalidPasswordAttempts", 5, false, 0);
			_PasswordAttemptWindow = GetIntValue(config, "passwordAttemptWindow", 10, false, 0);
			_MinRequiredPasswordLength = GetIntValue(config, "minRequiredPasswordLength", 7, false, 64);
			_MinRequiredNonalphanumericCharacters = GetIntValue(config, "minRequiredNonalphanumericCharacters", 0, true, 16);

			_HashAlgorithmType = config["hashAlgorithmType"];
			if (String.IsNullOrEmpty(_HashAlgorithmType))
			{
				_HashAlgorithmType = "SHA1";
			}

			_PasswordStrengthRegularExpression = config["passwordStrengthRegularExpression"];
			if (_PasswordStrengthRegularExpression != null)
			{
				_PasswordStrengthRegularExpression = _PasswordStrengthRegularExpression.Trim();
				if (_PasswordStrengthRegularExpression.Length != 0)
				{
					try
					{
						Regex regex = new Regex(_PasswordStrengthRegularExpression);
					}
					catch (ArgumentException e)
					{
						throw new ProviderException(e.Message, e);
					}
				}
			}
			else
			{
				_PasswordStrengthRegularExpression = string.Empty;
			}

			_AppName = config["applicationName"];
			if (string.IsNullOrEmpty(_AppName))
			{
				_AppName = GetDefaultAppName();
			}

			if (_AppName.Length > 255)
			{
				throw new ProviderException("Provider application name is too long, max length is 255.");
			}

			string strTemp = config["passwordFormat"];
			if (strTemp == null)
			{
				strTemp = "Hashed";
			}

			switch (strTemp)
			{
				case "Clear":
					_PasswordFormat = MembershipPasswordFormat.Clear;
					break;
				case "Encrypted":
					_PasswordFormat = MembershipPasswordFormat.Encrypted;
					break;
				case "Hashed":
					_PasswordFormat = MembershipPasswordFormat.Hashed;
					break;
				default:
					throw new ProviderException("Bad password format");
			}

			if (_PasswordFormat == MembershipPasswordFormat.Hashed && _EnablePasswordRetrieval)
			{
				throw new ProviderException("Provider cannot retrieve hashed password");
			}

			/*
						_DatabaseFileName = config["connectionStringName"];
						if (_DatabaseFileName == null || _DatabaseFileName.Length < 1)
						{
							throw new ProviderException("Connection name not specified");
						}

						string temp = AccessConnectionHelper.GetFileNameFromConnectionName(_DatabaseFileName, true);
						if (temp == null || temp.Length < 1)
						{
							throw new ProviderException("Connection string not found: " + _DatabaseFileName);
						}
						_DatabaseFileName = temp;

						// Make sure connection is good
						AccessConnectionHelper.CheckConnectionString(_DatabaseFileName);

						config.Remove("connectionStringName");
			*/

			config.Remove("enablePasswordRetrieval");
			config.Remove("enablePasswordReset");
			config.Remove("requiresQuestionAndAnswer");
			config.Remove("applicationName");
			config.Remove("requiresUniqueEmail");
			config.Remove("maxInvalidPasswordAttempts");
			config.Remove("passwordAttemptWindow");
			config.Remove("passwordFormat");
			config.Remove("name");
			config.Remove("description");
			config.Remove("minRequiredPasswordLength");
			config.Remove("minRequiredNonalphanumericCharacters");
			config.Remove("passwordStrengthRegularExpression");
			config.Remove("hashAlgorithmType");
			if (config.Count > 0)
			{
				string attribUnrecognized = config.GetKey(0);
				if (!String.IsNullOrEmpty(attribUnrecognized))
				{
					throw new ProviderException("Provider unrecognized attribute: " + attribUnrecognized);
				}
			}

		}

		#endregion

		#region MembershipUser Conversion Methods (Edit these if you add properties to the User object)

		private UserVO ConvertMembershipUserToUserVO(MembershipUser membershipUser)
		{
			if (membershipUser == null) return null;
			UserVO userVO = new UserVO();
			if (membershipUser.ProviderUserKey != null)
			{
				userVO.Id = (long)membershipUser.ProviderUserKey;
			}
			userVO.Username = membershipUser.UserName;
			userVO.Email = membershipUser.Email;
			userVO.Comment = membershipUser.Comment;
			userVO.IsActive = !membershipUser.IsLockedOut;
			userVO.CreationDate = membershipUser.CreationDate;
			return userVO;
		}

		private MembershipUser ConvertUserVOToMembershipUser(UserVO userVO)
		{
			if (userVO == null) return null;
			DateTime createDate = DateTime.MinValue;
			if (userVO.CreationDate.HasValue) createDate = userVO.CreationDate.Value;
			return new DomainMembershipUser(this.Name,
											userVO.Username,
											userVO.Id,
											userVO.Email,
											string.Empty,
											userVO.Comment,
											true,
											!userVO.IsActive,
											DateTime.MinValue,
											createDate,
											DateTime.MinValue,
											DateTime.MinValue,
											DateTime.MinValue,
											userVO);
		}

		/// <summary>
		/// Converts a collection of user objects to ASP.NET MembershipUser objects
		/// </summary>
		/// <param name="users">The users.</param>
		/// <returns></returns>
		private MembershipUserCollection ConvertUserVOListToMembershipUserCollection(IList users)
		{
			MembershipUserCollection userCollection = new MembershipUserCollection();
			foreach (UserVO userVO in users)
			{
				userCollection.Add(ConvertUserVOToMembershipUser(userVO));
			}
			return userCollection;
		}

		#endregion

		#region MembershipProvider Implementation

		/// <summary>
		/// Verifies that the specified user name and password exist in the data source.
		/// </summary>
		/// <param name="username">The name of the user to validate.</param>
		/// <param name="password">The password for the specified user.</param>
		/// <returns>
		/// true if the specified username and password are valid; otherwise, false.
		/// </returns>
		public override bool ValidateUser(string username, string password)
		{
			password = EncodePassword(password, (int)_PasswordFormat, GetSalt());
            UserVO validatedUser = MembershipService.ValidateUser(username, password);
			return (validatedUser != null);
		}

		/// <summary>
		/// Adds a new membership user to the data source.
		/// </summary>
		/// <param name="username">The user name for the new user.</param>
		/// <param name="password">The password for the new user.</param>
		/// <param name="email">The e-mail address for the new user.</param>
		/// <param name="passwordQuestion">The password question for the new user.</param>
		/// <param name="passwordAnswer">The password answer for the new user</param>
		/// <param name="isApproved">Whether or not the new user is approved to be validated.</param>
		/// <param name="providerUserKey">The unique identifier from the membership data source for the user.</param>
		/// <param name="status">A <see cref="T:System.Web.Security.MembershipCreateStatus"></see> enumeration value indicating whether the user was created successfully.</param>
		/// <returns>
		/// A <see cref="T:System.Web.Security.MembershipUser"></see> object populated with the information for the newly created user.
		/// </returns>
		public override MembershipUser CreateUser(string username, string password, string email, string passwordQuestion, string passwordAnswer, bool isApproved, object providerUserKey, out MembershipCreateStatus status)
		{
			UserVO userVO = new UserVO();
			userVO.Username = username;
			userVO.Email = email;
			userVO.IsActive = !isApproved;
			userVO.CreationDate = DateTime.Now;

			string pass = EncodePassword(password, (int)_PasswordFormat, GetSalt());
			if (pass.Length > 128)
			{
				status = MembershipCreateStatus.InvalidPassword;
				return null;
			}

			if (MembershipService.GetUser(username) != null)
			{
				status = MembershipCreateStatus.DuplicateUserName;
				return null;
			}
			if (this.RequiresUniqueEmail && !string.IsNullOrEmpty(MembershipService.GetUserNameByEmail(email)))
			{
				status = MembershipCreateStatus.DuplicateEmail;
				return null;
			}

			MembershipService.CreateUser(userVO, pass);

			status = MembershipCreateStatus.Success;

			return ConvertUserVOToMembershipUser(userVO);

		}

		/// <summary>
		/// Gets the user name associated with the specified e-mail address.
		/// </summary>
		/// <param name="email">The e-mail address to search for.</param>
		/// <returns>
		/// The user name associated with the specified e-mail address. If no match is found, return null.
		/// </returns>
		public override string GetUserNameByEmail(string email)
		{
			return MembershipService.GetUserNameByEmail(email);
		}

		/// <summary>
		/// Changes a user's password
		/// </summary>
		/// <param name="strName">The username of the user</param>
		/// <param name="strOldPwd">The current password</param>
		/// <param name="strNewPwd">The new password</param>
		/// <returns></returns>
		public override bool ChangePassword(string strName, string strOldPwd, string strNewPwd)
		{
			strOldPwd = EncodePassword(strOldPwd, (int)_PasswordFormat, GetSalt());
			strNewPwd = EncodePassword(strNewPwd, (int)_PasswordFormat, GetSalt());
			return MembershipService.UpdatePassword(strName, strOldPwd, strNewPwd);
		}

		/// <summary>
		/// Gets a user.
		/// </summary>
		/// <param name="strName">The username of the user</param>
		/// <param name="isUserOnline">if set to <c>true</c> [is user online].</param>
		/// <returns></returns>
		public override MembershipUser GetUser(string strName, bool isUserOnline)
		{
			return ConvertUserVOToMembershipUser(MembershipService.GetUser(strName));
		}

		/// <summary>
		/// Deletes a user.
		/// </summary>
		/// <param name="strName">The username of the user</param>
		/// <param name="boolDeleteAllRelatedData">if set to <c>true</c> [bool delete all related data].</param>
		/// <returns></returns>
		public override bool DeleteUser(string strName, bool boolDeleteAllRelatedData)
		{
			MembershipService.DeleteUser(strName);
			return true;
		}

		/// <summary>
		/// Finds users by email.
		/// </summary>
		/// <param name="strEmailToMatch">The email to match.</param>
		/// <param name="iPageIndex">Index of the i page.</param>
		/// <param name="iPageSize">Size of the i page.</param>
		/// <param name="iTotalRecords">The i total records.</param>
		/// <returns></returns>
		public override MembershipUserCollection FindUsersByEmail(string strEmailToMatch, int iPageIndex, int iPageSize, out int iTotalRecords)
		{
			IList users = MembershipService.FindUsersByEmail(strEmailToMatch, iPageIndex, iPageSize);
			iTotalRecords = users.Count;
			return ConvertUserVOListToMembershipUserCollection(users);
		}

		/// <summary>
		/// Finds users by username
		/// </summary>
		/// <param name="strUsernameToMatch">The STR username to match.</param>
		/// <param name="iPageIndex">Index of the i page.</param>
		/// <param name="iPageSize">Size of the i page.</param>
		/// <param name="iTotalRecords">The i total records.</param>
		/// <returns></returns>
		public override MembershipUserCollection FindUsersByName(string strUsernameToMatch, int iPageIndex, int iPageSize, out int iTotalRecords)
		{
			IList users = MembershipService.FindUsersByName(strUsernameToMatch, iPageIndex, iPageSize);
			iTotalRecords = users.Count;
			return ConvertUserVOListToMembershipUserCollection(users);
		}

		/// <summary>
		/// Gets all users.
		/// </summary>
		/// <param name="iPageIndex">Index of the i page.</param>
		/// <param name="iPageSize">Size of the i page.</param>
		/// <param name="iTotalRecords">The i total records.</param>
		/// <returns></returns>
		public override MembershipUserCollection GetAllUsers(int iPageIndex, int iPageSize, out int iTotalRecords)
		{
			IList userList = MembershipService.GetAllUsers(iPageIndex, iPageSize);
			iTotalRecords = userList.Count;
			return ConvertUserVOListToMembershipUserCollection(userList);
		}

		/// <summary>
		/// Updates information about a user in the data source.
		/// </summary>
		/// <param name="user">A <see cref="T:System.Web.Security.MembershipUser"></see> object that represents the user to update and the updated information for the user.</param>
		public override void UpdateUser(MembershipUser user)
		{
			MembershipService.UpdateUser(ConvertMembershipUserToUserVO(user));
		}

		/// <summary>
		/// Unlocks the user.
		/// </summary>
		/// <param name="strUserName">The username of the user.</param>
		/// <returns></returns>
		public override bool UnlockUser(string strUserName)
		{
			UserVO u = MembershipService.GetUser(strUserName);
			u.IsActive = true;
			MembershipService.UpdateUser(u);
			return true;
		}

		/// <summary>
		/// Gets information from the data source for a user based on the unique identifier for the membership user. Provides an option to update the last-activity date/time stamp for the user.
		/// </summary>
		/// <param name="providerUserKey">The unique identifier for the membership user to get information for.</param>
		/// <param name="userIsOnline">true to update the last-activity date/time stamp for the user; false to return user information without updating the last-activity date/time stamp for the user.</param>
		/// <returns>
		/// A <see cref="T:System.Web.Security.MembershipUser"></see> object populated with the specified user's information from the data source.
		/// </returns>
		public override MembershipUser GetUser(object providerUserKey, bool userIsOnline)
		{
			long userId = (long)providerUserKey;
			return ConvertUserVOToMembershipUser(MembershipService.GetUser(userId));
		}

		///////////////////////////
		///////////////////////////

		// Methods that are not implemented by this provider
		// You may implement these if you need them

		public override int GetNumberOfUsersOnline()
		{
			throw new NotSupportedException("GetNumberOfUsersOnline()");
		}

		public override bool ChangePasswordQuestionAndAnswer(string strName, string strPassword, string strNewPwdQuestion, string strNewPwdAnswer)
		{
			throw new NotSupportedException("ChangePasswordQuestionAndAnswer(strName, strPassword, strNewPwdQuestion, strNewPwdAnswer)");
		}

		public override string GetPassword(string username, string answer)
		{
			throw new NotSupportedException("GetPassword(username, answer)");
		}

		public override string ResetPassword(string strName, string strAnswer)
		{
			throw new NotSupportedException("ResetPassword(strName, strAnswer)");
		}

		#endregion

		#region Utility Methods

		private string GetSalt()
		{
			return PASSWORD_SALT;
		}

		private string EncodePassword(string pass, int passwordFormat, string salt)
		{
			if (passwordFormat == 0) // MembershipPasswordFormat.Clear
				return pass;

			byte[] bIn = Encoding.Unicode.GetBytes(pass);
			byte[] bSalt = Convert.FromBase64String(salt);
			byte[] bAll = new byte[bSalt.Length + bIn.Length];
			byte[] bRet = null;

			Buffer.BlockCopy(bSalt, 0, bAll, 0, bSalt.Length);
			Buffer.BlockCopy(bIn, 0, bAll, bSalt.Length, bIn.Length);
			if (passwordFormat == 1)
			{ // MembershipPasswordFormat.Hashed
				HashAlgorithm s = HashAlgorithm.Create(_HashAlgorithmType);

				// If the hash algorithm is null (and came from config), throw a config exception
				if (s == null)
				{
					throw new ProviderException("Could not create a hash algorithm");
				}
				bRet = s.ComputeHash(bAll);
			}
			else
			{
				bRet = EncryptPassword(bAll);
			}

			return Convert.ToBase64String(bRet);
		}

		private string UnEncodePassword(string pass, int passwordFormat)
		{
			switch (passwordFormat)
			{
				case 0: // MembershipPasswordFormat.Clear:
					return pass;
				case 1: // MembershipPasswordFormat.Hashed:
					throw new ProviderException("Provider can not decode hashed password");
				default:
					byte[] bIn = Convert.FromBase64String(pass);
					byte[] bRet = DecryptPassword(bIn);
					if (bRet == null)
						return null;
					return Encoding.Unicode.GetString(bRet, PASSWORD_SALT.Length, bRet.Length - PASSWORD_SALT.Length);
			}
		}

		private string GetExceptionText(int status)
		{
			string key;
			switch (status)
			{
				case 0:
					return String.Empty;
				case 1:
					key = "User not found";
					break;
				case 2:
					key = "Wrong password";
					break;
				case 3:
					key = "Wrong answer";
					break;
				case 4:
					key = "Invalid password";
					break;
				case 5:
					key = "Invalid question";
					break;
				case 6:
					key = "Invalid answer";
					break;
				case 7:
					key = "Invalid email";
					break;
				default:
					key = "Unknown provider error";
					break;
			}
			return key;
		}

		private bool IsStatusDueToBadPassword(int status)
		{
			return (status >= 2 && status <= 6);
		}

		public virtual string GeneratePassword()
		{
			return Membership.GeneratePassword(
					  MinRequiredPasswordLength < DEFAULT_AUTOGENERATED_PASSWORD_SIZE ? DEFAULT_AUTOGENERATED_PASSWORD_SIZE : MinRequiredPasswordLength,
					  MinRequiredNonAlphanumericCharacters);
		}

		private static string GetDefaultAppName()
		{
			try
			{
				string appName = System.Web.HttpRuntime.AppDomainAppVirtualPath;
				if (appName == null || appName.Length == 0)
				{
					return "/";
				}
				else
				{
					return appName;
				}
			}
			catch
			{
				return "/";
			}
		}

		private static bool ValidateParameter(ref string param, int maxSize)
		{
			if (param == null)
			{
				return false;
			}

			if (param.Trim().Length < 1)
			{
				return false;
			}

			if (maxSize > 0 && param.Length > maxSize)
			{
				return false;
			}

			return true;
		}


		private static bool ValidateParameter(ref string param, bool checkForNull, bool checkIfEmpty, bool checkForCommas, int maxSize)
		{
			if (param == null)
			{
				if (checkForNull)
				{
					return false;
				}

				return true;
			}

			param = param.Trim();
			if ((checkIfEmpty && param.Length < 1) ||
				 (maxSize > 0 && param.Length > maxSize) ||
				 (checkForCommas && param.IndexOf(",") != -1))
			{
				return false;
			}

			return true;
		}

		private static void CheckParameter(ref string param, int maxSize, string paramName)
		{
			if (param == null)
			{
				throw new ArgumentNullException(paramName);
			}

			if (param.Trim().Length < 1)
			{
				throw new ArgumentException("The parameter '" + paramName + "' must not be empty.",
					paramName);
			}

			if (maxSize > 0 && param.Length > maxSize)
			{
				throw new ArgumentException("The parameter '" + paramName + "' is too long: it must not exceed " + maxSize.ToString(CultureInfo.InvariantCulture) + " chars in length.",
					paramName);
			}
		}

		private static void CheckParameter(ref string param, bool checkForNull, bool checkIfEmpty, bool checkForCommas, int maxSize, string paramName)
		{
			if (param == null)
			{
				if (checkForNull)
				{
					throw new ArgumentNullException(paramName);
				}

				return;
			}

			param = param.Trim();

			if (checkIfEmpty && param.Length < 1)
			{
				throw new ArgumentException("The parameter '" + paramName + "' must not be empty.",
					paramName);
			}

			if (maxSize > 0 && param.Length > maxSize)
			{
				throw new ArgumentException("The parameter '" + paramName + "' is too long: it must not exceed " + maxSize.ToString(CultureInfo.InvariantCulture) + " chars in length.",
					paramName);
			}

			if (checkForCommas && param.IndexOf(',') != -1)
			{
				throw new ArgumentException("The parameter '" + paramName + "' must not contain commas.",
					paramName);
			}
		}

		private static void CheckArrayParameter(ref string[] param, bool checkForNull, bool checkIfEmpty, bool checkForCommas, int maxSize, string paramName)
		{
			if (param == null)
			{
				throw new ArgumentNullException(paramName);
			}

			if (param.Length < 1)
			{
				throw new ArgumentException("The array parameter '" + paramName + "' should not be empty.", paramName);
			}

			for (int i = param.Length - 1; i >= 0; i--)
			{
				CheckParameter(ref param[i],
							   checkForNull,
							   checkIfEmpty,
							   checkForCommas,
							   maxSize,
							   paramName + "[ " + i.ToString(CultureInfo.InvariantCulture) + " ]");
			}

			for (int i = param.Length - 1; i >= 0; i--)
			{
				for (int j = i - 1; j >= 0; j--)
				{
					if (param[i].Equals(param[j]))
					{
						throw new ArgumentException("The array '" + paramName + "' should not contain duplicate values.",
							paramName);
					}
				}
			}
		}

		private static bool GetBooleanValue(NameValueCollection config, string valueName, bool defaultValue)
		{
			string sValue = config[valueName];
			if (sValue == null)
			{
				return defaultValue;
			}

			if (sValue == "true")
			{
				return true;
			}

			if (sValue == "false")
			{
				return false;
			}

			throw new Exception("The value must be a boolean for property '" + valueName + "'");
		}

		private static int GetIntValue(NameValueCollection config, string valueName, int defaultValue, bool zeroAllowed, int maxValueAllowed)
		{
			string sValue = config[valueName];

			if (sValue == null)
			{
				return defaultValue;
			}

			int iValue;
			try
			{
				iValue = Convert.ToInt32(sValue, CultureInfo.InvariantCulture);
			}
			catch (InvalidCastException e)
			{
				if (zeroAllowed)
				{
					throw new Exception("The value must be a positive integer for property '" + valueName + "'", e);
				}

				throw new Exception("The value must be a positive integer for property '" + valueName + "'", e);
			}

			if (zeroAllowed && iValue < 0)
			{
				throw new Exception("The value must be a non-negative integer for property '" + valueName + "'");
			}

			if (!zeroAllowed && iValue <= 0)
			{
				throw new Exception("The value must be a non-negative integer for property '" + valueName + "'");
			}

			if (maxValueAllowed > 0 && iValue > maxValueAllowed)
			{
				throw new Exception("The value is too big for '" + valueName + "' must be smaller than " + maxValueAllowed.ToString(CultureInfo.InvariantCulture));
			}

			return iValue;
		}

		#endregion


	}

}

