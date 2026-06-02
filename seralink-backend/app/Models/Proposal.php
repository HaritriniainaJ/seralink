<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Factories\HasFactory;
use Illuminate\Database\Eloquent\Model;

class Proposal extends Model
{
    use HasFactory;

    protected $fillable = [
        'job_listing_id',
        'freelance_id',
        'cover_letter',
        'budget',
        'deadline',
        'status',
    ];

    public function jobListing()
    {
        return $this->belongsTo(JobListing::class);
    }

    public function freelance()
    {
        return $this->belongsTo(User::class, 'freelance_id');
    }
}