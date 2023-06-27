--
-- PostgreSQL database dump
--

-- Dumped from database version 14.5 (Debian 14.5-1.pgdg110+1)
-- Dumped by pg_dump version 14.5 (Debian 14.5-1.pgdg110+1)

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- Name: messages; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.messages (
	message_id integer NOT NULL,
	message_type text NOT NULL,
	date_time timestamp without time zone,
	sender_type text NOT NULL,
	sender_id integer NOT NULL,
	message_body text NOT NULL
);

ALTER TABLE public.messages OWNER TO postgres;

--
-- Name: messages_message_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.messages_message_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.messages_message_id_seq OWNER TO postgres;

--
-- Name: messages_message_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.messages_message_id_seq OWNED BY public.messages.message_id;

--
-- Name: messages_message_id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.messages ALTER COLUMN message_id SET DEFAULT nextval('public.messages_message_id_seq'::regclass);

--
-- Name: messages messages_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.messages
    ADD CONSTRAINT events_pkey PRIMARY KEY (message_id);

--
-- Name: privateMessages; Type: TABLE; Schema: public; Owner: postgres
--


CREATE TABLE public.privateMessages (
    message_id integer NOT NULL,
    sender_id integer NOT NULL,
    receiver_id integer NOT NULL
);

ALTER TABLE public.privateMessages OWNER TO postgres;

--
-- Name: privateMessages privateMessages_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.privateMessages
    ADD CONSTRAINT privateMessages_pkey PRIMARY KEY (message_id);

--
-- Name: privateMessages privateMessages_message_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.privateMessages
    ADD CONSTRAINT privateMessages_message_id_fkey FOREIGN KEY (message_id) REFERENCES public.messages(message_id);

--
-- Name: groups; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.groups (
    group_id integer NOT NULL,
    group_name text NOT NULL,
    creation_date timestamp without time zone
);

ALTER TABLE public.groups OWNER TO postgres;

--
-- Name: groups_group_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.groups_group_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.groups_group_id_seq OWNER TO postgres;

--
-- Name: groups_group_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.groups_group_id_seq OWNED BY public.groups.group_id;

--
-- Name: groups group_id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.groups ALTER COLUMN group_id SET DEFAULT nextval('public.groups_group_id_seq'::regclass);

--
-- Name: groups groups_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.groups
    ADD CONSTRAINT groups_pkey PRIMARY KEY (group_id);

--
-- Name: groupMessages; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.groupMessages (
    group_id integer NOT NULL,
    message_id integer NOT NULL,
    sender_id integer NOT NULL
);

ALTER TABLE public.groupMessages OWNER TO postgres;

--
-- Name: groupMessages groupMessages_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.groupMessages
    ADD CONSTRAINT groupMessages_pkey UNIQUE (message_id);

--
-- Name: groupMessages groupMessages_message_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.groupMessages
    ADD CONSTRAINT groupMessages_message_id_fkey FOREIGN KEY (message_id) REFERENCES public.messages(message_id);


--
-- Name: groupMembers; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.groupMembers (
    group_id integer NOT NULL,
    player_id integer NOT NULL
);

ALTER TABLE public.groupMembers OWNER TO postgres;

--
-- Name: groupMessages groupMembers_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.groupMembers
    ADD CONSTRAINT groupMembers_pkey PRIMARY KEY (group_id, player_id);

--
-- Name: groupMembers groupMembers_group_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.groupMembers
    ADD CONSTRAINT groupMembers_group_id_fkey FOREIGN KEY (group_id) REFERENCES public.groups(group_id);


--
-- Data for Name: messages; Type: TABLE DATA; Schema: public; Owner: postgres
--



INSERT INTO public.messages (message_id, message_type, date_time, sender_type, sender_id, message_body) VALUES (1, 'private', '2022-11-15 12:30:00', 'World', 0, 'Hello there!'),
                                                                                                               (2, 'private', '2022-11-15 12:31:00', 'World', 0, 'Welcome to zone 1.'),
                                                                                                               (3, 'private', '2022-11-15 12:32:00', 'World', 0, 'We hope you have a wonderful time!'),
                                                                                                               (4, 'private', '2022-11-15 12:33:00', 'Player', 1, 'Let''s do this!'),
                                                                                                               (5, 'group', '2022-11-15 12:45:00', 'Player', 4, 'We are group 3.'),
                                                                                                               (6, 'group', '2022-11-15 12:46:00', 'Player', 3, 'Indeed we are.');

--
-- Data for Name: privateMessages; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.privateMessages (message_id, sender_id, receiver_id) VALUES (1, 0, 1), (2, 0, 1), (3, 0, 1), (4, 1, 2);

--
-- Data for Name: groups; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.groups (group_id, group_name, creation_date) VALUES (1, 'Raptors', '2022-11-15 13:30:00'), (2, 'Lions', '2022-11-15 13:32:00'), (3, 'Jaguars', '2022-11-15 13:36:00');

--
-- Data for Name: groupMessages; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.groupMessages (group_id, message_id, sender_id) VALUES (3, 5, 4), (3, 6, 3);


--
-- Data for Name: groupMembers; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.groupMembers (group_id, player_id) VALUES (1, 1), (1, 2),
                                                             (2, 1), (2, 2), (2, 3), (2, 4),
                                                             (3, 3), (3, 4);

--
-- Name: messages_message_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.messages_message_id_seq', 6, true);

--
-- Name: groups_group_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.groups_group_id_seq', 3, true);

--
-- PostgreSQL database dump complete
--