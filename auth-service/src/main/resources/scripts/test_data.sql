INSERT INTO public.users (email, password_hash, role)
SELECT
    'u_' || i || '@t.co',
    'shrt_cln_hsh',
    'user'
FROM generate_series(1, 1000000) AS i;