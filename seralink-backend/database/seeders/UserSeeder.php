<?php
namespace Database\Seeders;

use App\Models\User;
use Illuminate\Database\Seeder;
use Illuminate\Support\Facades\Hash;

class UserSeeder extends Seeder
{
    public function run(): void
    {
        // Admin
        User::create([
            'name'     => 'Admin SeraLink',
            'email'    => 'admin@seralink.mg',
            'password' => Hash::make('password'),
            'role'     => 'admin',
        ]);

        // 10 Freelances
        $freelances = [
            ['name' => 'Rakoto Jean',      'email' => 'rakoto@seralink.mg',    'bio' => 'Développeur web fullstack'],
            ['name' => 'Rabe Marie',       'email' => 'rabe@seralink.mg',      'bio' => 'Designer UI/UX'],
            ['name' => 'Andry Paul',       'email' => 'andry@seralink.mg',     'bio' => 'Développeur mobile Android'],
            ['name' => 'Soa Nivo',         'email' => 'soa@seralink.mg',       'bio' => 'Rédactrice web et SEO'],
            ['name' => 'Haja Tiana',       'email' => 'haja@seralink.mg',      'bio' => 'Comptable et financier'],
            ['name' => 'Fidy Aina',        'email' => 'fidy@seralink.mg',      'bio' => 'Développeur Laravel'],
            ['name' => 'Meva Lanto',       'email' => 'meva@seralink.mg',      'bio' => 'Graphiste et illustrateur'],
            ['name' => 'Tahina Rivo',      'email' => 'tahina@seralink.mg',    'bio' => 'Expert marketing digital'],
            ['name' => 'Noro Hasina',      'email' => 'noro@seralink.mg',      'bio' => 'Traductrice FR/EN/MG'],
            ['name' => 'Miora Vatosoa',    'email' => 'miora@seralink.mg',     'bio' => 'Développeuse React'],
        ];

        foreach ($freelances as $f) {
            User::create([
                'name'     => $f['name'],
                'email'    => $f['email'],
                'password' => Hash::make('password'),
                'role'     => 'freelance',
                'bio'      => $f['bio'],
                'location' => 'Antananarivo, Madagascar',
            ]);
        }

        // 5 Clients
        $clients = [
            ['name' => 'Entreprise Ny Asa',   'email' => 'nyasa@seralink.mg'],
            ['name' => 'StartUp Mada Tech',   'email' => 'madatech@seralink.mg'],
            ['name' => 'ONG Fampandrosoana',  'email' => 'fampan@seralink.mg'],
            ['name' => 'Cabinet Avotra',      'email' => 'avotra@seralink.mg'],
            ['name' => 'Boutique Ny Tanana',  'email' => 'tanana@seralink.mg'],
        ];

        foreach ($clients as $c) {
            User::create([
                'name'     => $c['name'],
                'email'    => $c['email'],
                'password' => Hash::make('password'),
                'role'     => 'client',
                'location' => 'Antananarivo, Madagascar',
            ]);
        }
    }
}