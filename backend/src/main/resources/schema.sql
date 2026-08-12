EXEC sys.sp_getapplock
    @Resource = 'contact_management_schema_init',
    @LockMode = 'Exclusive',
    @LockOwner = 'Session',
    @LockTimeout = -1;EXEC sys.sp_getapplock
    @Resource = 'contact_management_schema_init',
    @LockMode = 'Exclusive',
    @LockOwner = 'Session',
    @LockTimeout = -1;

IF NOT EXISTS (
    SELECT 1
    FROM sys.indexes
    WHERE name = 'UX_users_email'
      AND object_id = OBJECT_ID('dbo.users')
)
CREATE UNIQUE INDEX UX_users_email
ON dbo.users(email)
WHERE email IS NOT NULL;

IF NOT EXISTS (
    SELECT 1
    FROM sys.indexes
    WHERE name = 'UX_users_phone_number'
      AND object_id = OBJECT_ID('dbo.users')
)
CREATE UNIQUE INDEX UX_users_phone_number
ON dbo.users(phone_number)
WHERE phone_number IS NOT NULL;

EXEC sys.sp_releaseapplock
    @Resource = 'contact_management_schema_init',
    @LockOwner = 'Session';