<?php
namespace Database\Seeders;

use App\Models\JobListing;
use App\Models\User;
use Illuminate\Database\Seeder;

class JobListingSeeder extends Seeder
{
    public function run(): void
    {
        $clients = User::where('role', 'client')->get();

        $jobs = [
            ['title' => 'Création site web vitrine',         'category' => 'Développement web',    'budget_min' => 150000,  'budget_max' => 400000],
            ['title' => 'Application mobile Android',        'category' => 'Développement mobile', 'budget_min' => 500000,  'budget_max' => 1500000],
            ['title' => 'Design logo et charte graphique',   'category' => 'Design graphique',     'budget_min' => 80000,   'budget_max' => 200000],
            ['title' => 'Rédaction articles blog SEO',       'category' => 'Rédaction',            'budget_min' => 30000,   'budget_max' => 80000],
            ['title' => 'Gestion comptabilité mensuelle',    'category' => 'Comptabilité',         'budget_min' => 100000,  'budget_max' => 250000],
            ['title' => 'Développement API Laravel',         'category' => 'Développement web',    'budget_min' => 300000,  'budget_max' => 800000],
            ['title' => 'Community management Facebook',     'category' => 'Marketing digital',    'budget_min' => 50000,   'budget_max' => 150000],
            ['title' => 'Traduction documents FR vers EN',   'category' => 'Traduction',           'budget_min' => 20000,   'budget_max' => 60000],
            ['title' => 'Dashboard analytics React',         'category' => 'Développement web',    'budget_min' => 400000,  'budget_max' => 1000000],
            ['title' => 'Illustration personnages BD',       'category' => 'Design graphique',     'budget_min' => 60000,   'budget_max' => 180000],
            ['title' => 'Formation Excel avancé',            'category' => 'Formation',            'budget_min' => 40000,   'budget_max' => 100000],
            ['title' => 'Audit SEO site existant',           'category' => 'Marketing digital',    'budget_min' => 80000,   'budget_max' => 200000],
            ['title' => 'Développement plugin WordPress',    'category' => 'Développement web',    'budget_min' => 120000,  'budget_max' => 350000],
            ['title' => 'Montage vidéo promotionnel',        'category' => 'Vidéo',                'budget_min' => 100000,  'budget_max' => 300000],
            ['title' => 'Transcription audio en texte',      'category' => 'Rédaction',            'budget_min' => 15000,   'budget_max' => 40000],
            ['title' => 'Création boutique WooCommerce',     'category' => 'Développement web',    'budget_min' => 200000,  'budget_max' => 600000],
            ['title' => 'Campagne publicité Google Ads',     'category' => 'Marketing digital',    'budget_min' => 70000,   'budget_max' => 180000],
            ['title' => 'Développement chatbot Telegram',    'category' => 'Développement web',    'budget_min' => 250000,  'budget_max' => 700000],
            ['title' => 'Photo produits e-commerce',         'category' => 'Photographie',         'budget_min' => 50000,   'budget_max' => 150000],
            ['title' => 'Mise en page rapport annuel PDF',   'category' => 'Design graphique',     'budget_min' => 60000,   'budget_max' => 160000],
        ];

        foreach ($jobs as $i => $job) {
            JobListing::create([
                'client_id'   => $clients[$i % count($clients)]->id,
                'title'       => $job['title'],
                'description' => 'Mission : '.$job['title'].'. Nous recherchons un freelance qualifié basé à Madagascar.',
                'category'    => $job['category'],
                'budget_min'  => $job['budget_min'],
                'budget_max'  => $job['budget_max'],
                'budget_type' => 'fixed',
                'status'      => 'open',
                'deadline'    => now()->addDays(rand(15, 60)),
            ]);
        }
    }
}